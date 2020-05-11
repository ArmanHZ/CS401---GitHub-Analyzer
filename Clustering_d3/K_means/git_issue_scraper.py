import urllib.request
import bs4
from bs4 import BeautifulSoup as soup
import sys
import time


class Issue:
    def __init__(self, id, issue_link):
        self.id = id
        self.issue_link = issue_link
        self.commit_link = None     # May be null, since not all closed issues are legit and have a fixed commit.
        self.commits = []


git_url = "https://github.com"

# Enter url of closed issues
url = sys.argv[1]
number_of_pages = int(sys.argv[2])

if len(sys.argv) < 2:
    print("You must provide the link of closed issues and # of pages you want to analyze.")
    exit(-1)


issues = []
for i in range(number_of_pages):
    url_split = url.split("?")
    page_url = url_split[0] + "?page=" + str(i + 1) + "&" + url_split[1]   # TODO other URLs might have another ? which will cause a bug.

    u_client = urllib.request.urlopen(page_url)

    page_html = u_client.read()
    page_soup = soup(page_html, "html.parser")

    git_issues = page_soup.findAll("div", { "id": lambda l: l and l.startswith("issue") })

    for issue in git_issues:
        a_tag = issue.findAll("a", { "id": True })
        issue_id = a_tag[0].get("id")   # Each issue div has one "a" tag, so it is safe to use [0]
        issue_id = issue_id.split("_")[1]
        issue_href = git_url + a_tag[0].get("href")
        issues.append(Issue(issue_id, issue_href))


    for issue in issues:
        u_client = urllib.request.urlopen(issue.issue_link)
        current_html = u_client.read()
        current_soup = soup(current_html, "html.parser")
        current_commit_link = current_soup.findAll("code")
        for line in current_commit_link:
            a_tag = line.find("a", { "data-hovercard-type": "commit"})
            if a_tag is not None:
                tag_split = str(a_tag).split(" ")
                for tag in tag_split:
                    if "href" in tag:
                        issue.commit_link = git_url + tag.split("\"")[1]


    for issue in issues:
        if issue.commit_link is not None:
            u_client = urllib.request.urlopen(issue.commit_link)
            current_html = u_client.read()
            current_soup = soup(current_html, "html.parser")
            changed_files = current_soup.findAll("a", { "class": "link-gray-dark" })
            for file_ in changed_files:
                if len(file_) is 1:
                    try:
                        split = str(file_).split(" ")
                        split = split[3].split("\"")[1]
                        issue.commits.append(split)
                    except:
                        print("issue at: " + issue.id + "\t" + issue.issue_link)

    file = open("issue_data.txt", "a")
    for issue in issues:
        data_str = str(issue.id) + "\n" + str(issue.issue_link) + "\n" + str(issue.commit_link) + "\n"
        file.write(data_str)
        commit_str = ""
        for commit in issue.commits:
            commit_str += commit + ", "
        file.write(commit_str[:-2] + "\n")
        file.write("\n")
    
    file.close()
    issues = []
    time.sleep(35)



# Print function
# for issue in issues:
#     print(issue.id)
#     print(issue.issue_link)
#     print(issue.commit_link)
#     print(issue.commits)
#     print()


u_client.close()

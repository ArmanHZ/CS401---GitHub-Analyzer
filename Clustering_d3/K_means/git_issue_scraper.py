import urllib.request
import bs4
from bs4 import BeautifulSoup as soup


class Issue:
    def __init__(self, id, issue_link, commits):
        self.id = id
        self.issue_link = issue_link
        self.commit_link = None     # May be null, since not all closed issues are legit and have a fixed commit.
        self.commits = commits


git_url = "https://github.com"

# Enter url of closed issues
url = "https://github.com/vim/vim/issues?q=is%3Aissue+is%3Aclosed"

u_client = urllib.request.urlopen(url)

page_html = u_client.read()
page_soup = soup(page_html, "html.parser")

git_issues = page_soup.findAll("div", { "id": lambda l: l and l.startswith("issue") })
issues = []

for issue in git_issues:
    a_tag = issue.findAll("a", { "id": True })
    issue_id = a_tag[0].get("id")   # Each issue div has one "a" tag, so it is safe to use [0]
    issue_id = issue_id.split("_")[1]
    issue_href = git_url + a_tag[0].get("href")
    issues.append(Issue(issue_id, issue_href, None))

# TODO loop through issue links and find which files were committed per issue.

for issue in issues[:5]:
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




u_client.close()

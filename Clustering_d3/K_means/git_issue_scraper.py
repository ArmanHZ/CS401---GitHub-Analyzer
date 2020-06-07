import urllib.request
import bs4
from bs4 import BeautifulSoup as soup
import sys
import time

class Issue:
    issue_id = ""
    issue_href = ""
    commit_href = ""
    committed_files = []

    def __str__(self):
        return_value = "Issue id: " + self.issue_id + "\nIssue href: " + self.issue_href + "\nCommit href: " + self.commit_href + "\nCommitted files:  "
        for commit in self.committed_files:
            return_value += commit + ", "
        return return_value[:-2]


github_link = "https://github.com"
# unedited_url = urllib.request.urlopen("https://github.com/vim/vim/issues?q=is%3Aissue+is%3Aclosed")
# url_soup = soup(unedited_url, "html.parser")

# issues = url_soup.findAll("a", { "id": lambda l: l and l.startswith("issue") })

scraped_issues = []

# Add page loop from here

page_count = 97
current_page = 58
# page_url = "https://github.com/vim/vim/issues?page=1&q=is%3Aissue+is%3Aclosed"
page_url_first_part = "https://github.com/vim/vim/issues?page="
page_url_second_part = "&q=is%3Aissue+is%3Aclosed"

file = open("scraped_data_3.txt", "w", encoding="utf-8")
for current_page in range(current_page, page_count):
    page_url = page_url_first_part + str(current_page) + page_url_second_part
    # print(page_url)
    unedited_url = urllib.request.urlopen(page_url)
    url_soup = soup(unedited_url, "html.parser")

    issues = url_soup.findAll("a", { "id": lambda l: l and l.startswith("issue") })

    for issue in issues:
        issue_obj = Issue()
        issue_obj.issue_id = str(issue["id"]).split("_")[1]
        issue_obj.issue_href = github_link + issue["href"]
        scraped_issues.append(issue_obj)

    for issue in scraped_issues:
        issue_href = issue.issue_href
        unedited_url = urllib.request.urlopen(issue_href)
        url_soup = soup(unedited_url, "html.parser")
        commit_link_tag = url_soup.findAll("a", { "data-hovercard-type": "commit" })
        if len(commit_link_tag) is not 0:
            commit_link = commit_link_tag[-1]
            commit_link = commit_link["href"]
            if github_link not in commit_link:
                commit_link = github_link + commit_link
            issue.commit_href = commit_link
        time.sleep(10)


    for issue in scraped_issues:
        commit_href = issue.commit_href
        if commit_href is not "":
            unedited_url = urllib.request.urlopen(commit_href)
            url_soup = soup(unedited_url, "html.parser")
            commits_tag = url_soup.findAll("a", { "title": True, "class": "link-gray-dark" } )
            commits = []
            for tag in commits_tag:
                commits.append(tag["title"])
            issue.committed_files = commits
        time.sleep(10)


    for issue in scraped_issues:
        result_string = issue.issue_id + "\n" + issue.issue_href + "\n"
        if issue.commit_href is not "":
            result_string += issue.commit_href + "\n"
            for commit in issue.committed_files:
                result_string += commit + " "
            result_string += "\n"
        file.write(result_string + "\n")
    
    current_page += 1
    scraped_issues = []
    # time.sleep(60)


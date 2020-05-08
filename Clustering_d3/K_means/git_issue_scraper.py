import urllib.request
import bs4
from bs4 import BeautifulSoup as soup


class Issue:
    def __init__(self, id, link, commits):
        self.id = id
        self.link = link
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



u_client.close()


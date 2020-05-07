import urllib.request
import bs4
from bs4 import BeautifulSoup as soup

# Enter url of closed issues
url = "https://github.com/vim/vim/issues?q=is%3Aissue+is%3Aclosed"

u_client = urllib.request.urlopen(url)

page_html = u_client.read()

page_soup = soup(page_html, "html.parser")

git_issues = page_soup.findAll("div", { "id": lambda l: l and l.startswith("issue") })
issues_and_hrefs = []

for issue in git_issues:
    a_tag = issue.findAll("a", { "id": True })
    issue_id = a_tag[0].get("id")   # Each issue div has one "a" tag, so it is safe to use [0]
    issue_href = a_tag[0].get("href")
    issue_and_href = { "issue": issue_id, "href": issue_href }
    issues_and_hrefs.append(issue_and_href)



u_client.close()


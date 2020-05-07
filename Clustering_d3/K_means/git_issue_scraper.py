import urllib.request
import bs4
from bs4 import BeautifulSoup as soup

# Enter url of closed issues
url = "https://github.com/vim/vim/issues?q=is%3Aissue+is%3Aclosed"

u_client = urllib.request.urlopen(url)

page_html = u_client.read()

# print(page_html)

page_soup = soup(page_html, "html.parser")

git_issues = page_soup.findAll("div", { "id": lambda l: l and l.startswith("issue") })

sinlge_issue = git_issues[0].findAll("a", { "id": True })
print(sinlge_issue[0].get("id"))

u_client.close()


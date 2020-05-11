class Issue:
    id = None
    issue_link = None
    commit_link = None
    commits = None


line_counter = 0
issues = []
with open("issue_data.txt") as fp:
    new_issue = Issue()
    for line in fp:
        if line_counter is 0:
            new_issue.id = line[:-1]
            line_counter += 1
        elif line_counter is 1:
            new_issue.issue_link = line[:-1]
            line_counter += 1
        elif line_counter is 2:
            new_issue.commit_link = line[:-1]
            line_counter += 1
        elif line_counter is 3:
            new_issue.commits = line[:-1]
            line_counter += 1
        else:
            issues.append(new_issue)
            line_counter = 0
            new_issue = Issue()


file = open("issue.json", "w")
file.write("{\n\t\"issues\":[\n")

for issue in issues[:-1]:
    issue_json = "\t\t{\"id\": \"" + issue.id + "\", \"issue_link\": \"" + issue.issue_link + "\", \"commit_link\": \"" + issue.commit_link + "\", \"commits\": \"" + issue.commits + "\"},"
    file.write(issue_json + "\n")

last_issue = "\t\t{\"id\": \"" + issues[-1].id + "\", \"issue_link\": \"" + issues[-1].issue_link + "\", \"commit_link\": \"" + issues[-1].commit_link + "\", \"commits\": \"" + issues[-1].commits + "\"}"
file.write(last_issue)

file.write("\t]\n")
file.write("}\n")
file.close()

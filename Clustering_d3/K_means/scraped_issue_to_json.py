class Issue:
    id = None
    issue_link = None
    commit_link = None
    commits = None

    def __str__(self):
        if self.commit_link is None:
            return str(self.id) + "\n" + str(self.issue_link)
        else:
            return str(self.id) + "\n" + str(self.issue_link) + "\n" + str(self.commit_link) + "\n" + str(self.commits)


line_counter = 0
issue_counter = 0
issues = []
issues.append(Issue())
with open("issue_data.txt") as fp:
    for line in fp:
        if line is "\n":
            line_counter = 0
            issues.append(Issue())
            issue_counter += 1
        else:
            if line_counter is 0:
                issues[issue_counter].id = line[:-1]
            if line_counter is 1:
                issues[issue_counter].issue_link = line[:-1]
            if line_counter is 2:
                issues[issue_counter].commit_link = line[:-1]
            if line_counter is 3:
                issues[issue_counter].commits = line[:-1]
            line_counter += 1


file = open("issue.json", "w")
file.write("{\n\t\"issues\":[\n")

for issue in issues[:-1]:
    issue_json = "\t\t{\"id\": \"" + str(issue.id) + "\", \"issue_link\": \"" + str(issue.issue_link) + "\", \"commit_link\": \"" + str(issue.commit_link) + "\", \"commits\": \"" + str(issue.commits) + "\"},"
    file.write(issue_json + "\n")

last_issue = "\t\t{\"id\": \"" + str(issues[-1].id) + "\", \"issue_link\": \"" + str(issues[-1].issue_link) + "\", \"commit_link\": \"" + str(issues[-1].commit_link) + "\", \"commits\": \"" + str(issues[-1].commits) + "\"}"
file.write(last_issue)

file.write("\t]\n")
file.write("}\n")
file.close()

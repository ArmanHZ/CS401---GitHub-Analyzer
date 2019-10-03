import sys

# Some Git Bash commands used in the same order used in .sh file
# H  = Commit hash
# an = Author name
# cn = Committer name
# cd = Commit date
# s  = Subject (Commit message)


class DataFormat:
    commit_hash = None
    author_name = None
    committer_name = None
    commit_date = None
    commit_message = None
    edited_files = []

    def reformat_data(self, information_part, changed_files_part):
        self.commit_hash = information_part[0]
        self.author_name = information_part[1]
        self.committer_name = information_part[2]
        self.commit_date = information_part[3]
        self.commit_message = information_part[4]
        self.edited_files = changed_files_part

    def to_string(self):
        result_string = "Commit hash: " + self.commit_hash + "\n"\
                        "Author name: " + self.author_name + "\n"\
                        "Committer name: " + self.committer_name + "\n"\
                        "Commit date: " + self.commit_date + "\n"\
                        "Commit message: " + self.commit_message + "\n"\
                        "Edited files: "
        for i in self.edited_files:
            result_string += i + ", "
        result_string = result_string[:-2]  # Remove the last ", "
        result_string += "\n"
        return result_string


""" End of Class """


def date_non_restricted_format(file):
    file = open("FormattedData.txt", "r", encoding="utf-8")
    separated_commits = file.read().split("#")
    file.close()
    final_string = ""
    for i in separated_commits[1:]:   # Due to formatting, the index [0] is an empty string.
        temp = DataFormat()
        separated_information = i.split("\n")   # Splits the commit info and changed files by the commit
        separated_information = list(filter(None, separated_information))   # Remove any empty strings
        information_part = separated_information[0].split("|")
        temp.reformat_data(information_part, separated_information[1:])
        final_string += temp.to_string() + "##############################\n"
    file = open("FormattedData.txt", "w", encoding="utf-8")
    file.write(final_string[:-32])  # 32 = number of # + 2*\n
    file.close()


def date_restricted_format(file):
    file = open("FormattedDataDateRestricted.txt", "r", encoding="utf-8")


def main():
    date_restricted = False
    if len(sys.argv) > 1:
        date_restricted = True

    file = None
    if date_restricted:
        date_restricted_format(file)
    else:
        date_non_restricted_format(file)


if __name__ == "__main__":
    main()

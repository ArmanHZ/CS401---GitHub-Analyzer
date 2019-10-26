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
    status =

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
                     


""" End of DataFormat Class """


def format_file(file_name):
    current_string = ""
    open("final_dump.txt", "w", encoding="utf-8")   # Just to reset the final_dump.txt file.
    with open(file_name, "r", encoding="utf-8") as fp:
        for line in fp:
            if line != '\n':
                current_string += line
            else:
                final_string = format_commits(current_string)
                file = open("final_dump.txt", "a", encoding="utf-8")
                file.write(final_string)
                file.close()
                current_string = ""


def format_commits(current_string):
    temp = DataFormat()
    current_string_split = current_string.split("\n")
    current_string_split = list(filter(None, current_string_split))  # Remove any empty strings
    information_part = current_string_split[0].split("|")
    temp.reformat_data(information_part, current_string_split[1:])
    return_string = temp.to_string() + "##############################\n"
    return return_string


def main():
    to_be_searched = ["before", "after"]
    if any(x in str(sys.argv) for x in to_be_searched):
        file_name = "FormattedDataDateRestricted.txt"
        format_file(file_name)
    elif "-nlog" in str(sys.argv):
        file_name = "FormattedDataNoMerges.txt"
        format_file(file_name)
    else:
        file_name = "FormattedData.txt"
        format_file(file_name)
    print("Done!")


if __name__ == "__main__":
    main()

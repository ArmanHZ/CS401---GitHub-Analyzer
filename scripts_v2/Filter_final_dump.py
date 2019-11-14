import sys


class FinalDumpFormat:
    commit_hash = None
    author_name = None
    committer_name = None
    commit_date = None
    commit_message = None
    edited_files = None

    def set_commit_hash(self, string):
        self.commit_hash = string

    def set_author_name(self, string):
        self.author_name = string

    def set_committer_name(self, string):
        self.committer_name = string

    def set_commit_date(self, string):
        self.commit_date = string

    def set_commit_message(self, string):
        self.commit_message = string

    def set_edited_files(self, string):
        self.edited_files = string


separator = "##############################"


def filter_final_dump(filters):
    with open("final_dump.txt", 'r', encoding="utf-8") as fp:
        temp = FinalDumpFormat()
        count = 0
        for line in fp:
            if line[:-1] != separator:
                data_to_correct_place(count, temp, line)
                count += 1
            elif line[:-1] == separator:
                filter_file(filters, temp)
                temp = FinalDumpFormat()
                count = 0


def data_to_correct_place(count, file_dump_format, line):
    line = line[:-1]    # Getting rid of "\n"
    if count == 0:
        file_dump_format.set_commit_hash(line)
    elif count == 1:
        file_dump_format.set_author_name(line)
    elif count == 2:
        file_dump_format.set_committer_name(line)
    elif count == 3:
        file_dump_format.set_commit_date(line)
    elif count == 4:
        file_dump_format.set_commit_message(line)
    elif count == 5:
        file_dump_format.set_edited_files(line)
    else:
        exit(-1)


def filter_file(filters, final_dump_format):
    filters_as_list = list(filters)
    every_edited_file = list(final_dump_format.edited_files[14:].replace(" ", "").split(","))
    result = ""
    for i in filters_as_list:
        for j in every_edited_file:
            if i in j and j.endswith(i):
                result += j + ", "
    if result:
        final_dump_format.set_edited_files(result[:-2])
        write_to_file(final_dump_format)


def write_to_file(final_dump_format):
    file = open("Filtered_final_dump.txt", 'a', encoding="utf-8")
    file.write("Commit hash: " + final_dump_format.commit_hash + "\n"
               "Author name: " + final_dump_format.author_name + "\n"
               "Committer name: " + final_dump_format.committer_name + "\n"
               "Commit date: " + final_dump_format.commit_date + "\n"
               "Commit message: " + final_dump_format.commit_message + "\n"
               "Edited files: " + final_dump_format.edited_files.__str__() + "\n"
               "##############################\n")


def main():
    temp = open("Filtered_final_dump.txt", 'w', encoding="utf-8")   # Reset / create the file
    temp.close()
    filter_final_dump(sys.argv[2:])


if __name__ == '__main__':
    main()


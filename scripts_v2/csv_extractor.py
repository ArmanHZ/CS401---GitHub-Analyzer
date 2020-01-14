import re

unique_commit_hashes = []
unique_committer_names = []
unique_file_names = []


def main():
    reset_files()
    with open("data_graph.txt", 'r+', encoding="utf-8") as fp:
        commit = ""
        for line in fp:
            if line[0] != "\n":
                commit += line
            else:
                parse_commit(commit)
                commit = ""
        parse_commit(commit)
        write_other_csv_files()


def reset_files():
    main_csv = open("main_csv.csv", 'w', encoding="utf-8")
    main_csv.write("commithash;committername;filename;status;date\n")
    main_csv.close()

    commit_hashes_csv = open("commit_hashes.csv", 'w', encoding="utf-8")
    commit_hashes_csv.write("commithash\n")
    commit_hashes_csv.close()

    unique_committer_names_csv = open("committer_names.csv", 'w', encoding="utf-8")
    unique_committer_names_csv.write("committername\n")
    unique_committer_names_csv.close()

    unique_file_names_csv = open("file_names.csv", 'w', encoding="utf-8")
    unique_file_names_csv.write("filename\n")
    unique_file_names_csv.close()


def parse_commit(commit):
    if len(commit) > 0:
        commit_as_list = commit.split("\n")
        commit_as_list = list(filter(None, commit_as_list))  # Remove empty strings
        commit_info = commit_as_list[0].split('Ω')
        commit_hash = commit_info[0]
        name = commit_info[1]
        date = commit_info[2]
        files = commit_as_list[1:]
        files = parse_files(files)

        unique_commit_hashes.append(commit_hash)
        if name not in unique_committer_names:
            unique_committer_names.append(name)
        for i in range(0, len(files), 2):
            if files[i + 1] not in unique_file_names:
                unique_file_names.append(files[i + 1])

        main_csv = open("main_csv.csv", 'a', encoding="utf-8")
        for i in range(0, len(files), 2):
            main_csv.write(commit_hash + ";" + name + ";" + files[i + 1] + ";" + files[i] + ";" + date + "\n")
        main_csv.close()


def parse_files(files):
    return_list = []
    for file in files:
        file = file.split("\t")
        status = file[0]
        name = file[1]
        return_list.append(status)
        return_list.append(name)
    return return_list


def write_other_csv_files():
    commit_hashes_csv = open("commit_hashes.csv", 'a', encoding="utf-8")
    for i in unique_commit_hashes:
        commit_hashes_csv.write(i + "\n")
    commit_hashes_csv.close()

    unique_committer_names_csv = open("committer_names.csv", 'a', encoding="utf-8")
    for i in unique_committer_names:
        unique_committer_names_csv.write(i + "\n")
    unique_committer_names_csv.close()

    unique_file_names_csv = open("file_names.csv", 'a', encoding="utf-8")
    for i in unique_file_names:
        unique_file_names_csv.write(i + "\n")
    unique_file_names_csv.close()


if __name__ == '__main__':
    main()

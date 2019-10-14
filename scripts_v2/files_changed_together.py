

LINE_SEPARATOR = '##############################'


class CSVFormat:
    commit_date = None
    num_files_changed_together = None
    files_changed_together = None

    def __init__(self, commit_date, num_files_changed_together, files_changed_together):
        self.commit_date = commit_date
        self.num_files_changed_together = num_files_changed_together
        self.files_changed_together = files_changed_together

    def __str__(self):
        return_string = self.commit_date + ',' + self.num_files_changed_together + ','
        for i in self.files_changed_together:
            return_string += i
        return return_string + '\n'


def read_csv_file(file_name):
    current_string = ''
    with open(file_name, 'r', encoding='utf-8') as fp:
        for line in fp:
            if line[:-1] == LINE_SEPARATOR:    # line[:-1] because line has '\n' at the end.
                current_string_as_list = current_string.split('\n')
                csv_format = string_to_csv_format(current_string_as_list)
                if int(csv_format.num_files_changed_together) > 1:
                    write_csv_format_to_file(csv_format.__str__())
                current_string = ''
            else:
                current_string += line


def string_to_csv_format(input_list):
    commit_date = input_list[3]
    commit_date = git_date_to_python_date(commit_date)
    files_changed_together = input_list[5][14:]
    num_files_changed_together = len(files_changed_together.split(', ')).__str__()
    return CSVFormat(commit_date, num_files_changed_together, files_changed_together.replace(' ', ''))


def git_date_to_python_date(commit_date):
    date_split = commit_date.split(' ')
    year = date_split[6]
    month = date_split[3]
    month = month_as_number(month).__str__()
    day = date_split[4]
    return str(year + '/' + month + '/' + day)


def month_as_number(month):
    switch = {
        'Jan': 1,
        'Feb': 2,
        'Mar': 3,
        'Apr': 4,
        'May': 5,
        'Jun': 6,
        'Jul': 7,
        'Aug': 8,
        'Sep': 9,
        'Oct': 10,
        'Nov': 11,
        'Dec': 12
    }
    return switch.get(month, 'Nothing')


def write_csv_format_to_file(csv_format):
    with open('files_changed_together.csv', 'w', encoding='utf-8') as fp:
        fp.write(csv_format)


def main():
    read_csv_file('final_dump.txt')


if __name__ == '__main__':
    main()

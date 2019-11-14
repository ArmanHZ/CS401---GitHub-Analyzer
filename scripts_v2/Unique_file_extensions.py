import os


extensions = []


def main():
    with open("Unique_file_extensions_temp.txt", 'r', encoding="utf-8") as fp:
        for line in fp:
            extension = line.split('.')
            extension = "." + extension[-1][:-1]    # Get the extension part and add a dot.
            if extension not in extensions:
                extensions.append(extension)
    append_extensions_to_file()


def append_extensions_to_file():
    for i in extensions:
        with open("Unique_file_extensions.txt", 'a', encoding="utf-8") as fp:
            fp.write(i + "\n")


if __name__ == '__main__':
    file = open("Unique_file_extensions.txt", 'w', encoding="utf-8")    # Just to generate the file
    file.close()
    main()
    os.remove("Unique_file_extensions_temp.txt")

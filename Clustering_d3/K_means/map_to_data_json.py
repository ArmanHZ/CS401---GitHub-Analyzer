nodes = []
links = []


def main():
    init_nodes()
    init_links()
    create_data_file()


def init_nodes():
    with open("map.txt") as fp:
        for line in fp:
            split_line = line.split(' ')
            for i in range(2):
                node = make_node(split_line[i])
                if not is_in_nodes(node):
                    nodes.append(node)


def make_node(node_name):
    return "{\"id\": \"" + node_name + "\"},"


def is_in_nodes(node):
    return node in nodes


def init_links():
    with open("map.txt") as fp:
        for line in fp:
            split_line = line.split(' ')
            link = make_link(split_line[0], split_line[1], split_line[2])
            if not is_in_links(link):
                links.append(link)


def make_link(source, targer, weight):
    return "{\"source\": \"" + source + "\", \"target\": \"" + targer + "\", \"value\": " + weight.strip("\n") + "},"


def is_in_links(link):
    return link in links


def create_data_file():
    file = open("data.json", 'w', encoding="utf-8")
    file.write("{\n")
    file.write("\t\"nodes\": [\n")
    for i in nodes[:-1]:
        file.write("\t\t" + i + "\n")
    file.write("\t\t" + nodes[-1][:-1] + "\n")
    file.write("\t],\n")

    file.write("\t\"links\": [\n")
    for i in links[:-1]:
        file.write("\t\t" + i + "\n")
    file.write("\t\t" + links[-1][:-1] + "\n")
    file.write("\t]\n")
    file.write("}")


if __name__ == "__main__":
    main()

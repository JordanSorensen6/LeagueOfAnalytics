import re
import json
import glob


def string_to_num(number):
    return float(number) if '.' in number else int(number)


def get_all_txt_files():
    return glob.glob("*.txt")


if __name__ == "__main__":
    txt_files = get_all_txt_files()
    scores = []
    for file in txt_files:
        with open(file) as data:
            for line in data:
                numbs = re.findall(r"[-]?\d*\.\d+|\d+", line)
                score = {"score": string_to_num(numbs[0]), "wins": string_to_num(numbs[1]), "losses": string_to_num(numbs[2])}
                scores.append(score)
        txt_file_name_length = len(file)
        json_file_name = file[:txt_file_name_length - 3] + 'json'
        with open(json_file_name, 'w') as json_file:
            json.dump(scores, json_file, indent=4)
        scores.clear()

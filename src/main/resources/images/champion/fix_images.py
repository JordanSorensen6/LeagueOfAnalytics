import os
import sys
import re
import requests
import urllib.request

def format_name(name):
	regex = re.compile('[^a-zA-Z]')
	return regex.sub('', name).lower()

def get_champions(patch, updates):
	# format the address to the current patch: ie, .../cdn/CURRENT.PATCH.1/data/en_US/champion.json
	r = requests.get('http://ddragon.leagueoflegends.com/cdn/{}/data/en_US/champion.json'.format(patch))
	champlist = {}
	if r.status_code == 200:
		json = r.json()['data']
		for champ in json:
			champlist[format_name(json[champ]['name'])] = json[champ]['image']['full']
		for name in updates:
			if name in champlist:
				champlist[format_name(updates[name])] = champlist.pop(name)
	else:
		print('Failed to retrieve champion json data from data dragon')

	return champlist

def import_images(patch):
	url = 'http://ddragon.leagueoflegends.com/cdn/{}/img/champion/{}'
	updates = {'nunuwillump': 'nunu'}
	champlist = get_champions(patch, updates)
	for champ in champlist:
		urllib.request.urlretrieve(url.format(patch, champlist[champ]), '{}.png'.format(champ))


if __name__ == '__main__':
	if len(sys.argv) == 3:
		if sys.argv[1] == '-import':
			import_images(sys.argv[2])
	for filename in os.listdir("./"):
		if filename != "placeholderOpponent.png" and filename != "placeholderTeam.png" and filename[-3:] == "png":
			os.replace(filename, filename.lower())
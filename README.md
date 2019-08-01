# Zamenhof
A Wiktionary dump parser for generating simple language pair dictionaries

## Setup:
Install the python requirements:
```bash
pip3 install -r scripts/requirements.txt
```
Install the maven requirements:
```bash
mvn clean install
```
Download the Wiktionary dumps and run the parser:
```bash
python3 scripts/run.py --lang LANG --outputdir OUTPUT_DIRECTORY --targetdir BUILD_DIRECTORY
```






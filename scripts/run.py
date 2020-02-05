import argparse
import bz2
import logging
import os
import regex as re
import subprocess
import sys
import traceback
import wget

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger("log")

def download(lang,dir):
    base_url = "https://dumps.wikimedia.org/{}wiktionary/latest/{}wiktionary-latest-pages-articles-multistream.xml.bz2".format(lang,lang)
    logger.info("--> downloading "+lang+" dump...")
    return wget.download(base_url, out=dir)

def decompress(lang,bz2_file):
    xml_file = bz2_file[:-len('.bz2')]
    logger.info("--> decompressing "+lang+" dump...")
    with bz2.BZ2File(bz2_file) as f1, open(xml_file, 'wb') as f2:
        f2.write(f1.read())
    return xml_file

def main():
    parser = argparse.ArgumentParser(description="Usage: {0} [options] [input [output]]".format(sys.argv[0]))
    parser.add_argument("--sourcelanguage",help="Source translation language", default=None, required=True)
    parser.add_argument("--targetlanguage",help="Target translation language", default=None, required=False)
    parser.add_argument("--dir", help="Path to store the downloaded and decompressed dumps", default=None, required=True)
    parser.add_argument("--targetdir", help="Directory for compiled java", default=None, required=True)
    args = parser.parse_args()
    xml_file = "{}/{}wiktionary-latest-pages-articles-multistream.xml".format(args.dir,args.sourcelanguage)
    if os.path.isfile(xml_file):
        logger.info("--> "+args.sourcelanguage+" dump found!")
    else:
        bz2_file = download(args.sourcelanguage,args.dir)
        xml_file = decompress(args.sourcelanguage,bz2_file)
    #windows systems need a different divider than unix systems
    divider = ";" if sys.platform.startswith('win') else ":"
    target = divider.join([os.path.join(args.targetdir, j) for j in os.listdir(args.targetdir) if re.match(r".+\.jar", j)])
    #run java
    env = dict(os.environ)
    java_arguments = ['java', '-Xmx1g','-cp', target, 'com.github.kenmurrell.zamenhof.Generate', "-p", args.dir, "-w",xml_file]
    if args.targetlanguage:
        java_arguments.extend(["-l",args.sourcelanguage+"-"+args.targetlanguage])
    logger.info("--> Starting JVM...")
    subprocess.call(java_arguments, env=env)

if __name__ == '__main__':
    try:
        main()
    except Exception as ex:
        tb = traceback.format_exc()
        logger.error(tb)
        sys.exit(1)

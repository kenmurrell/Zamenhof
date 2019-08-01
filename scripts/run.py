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


def main():
    parser = argparse.ArgumentParser(description="Usage: {0} [options] [input [output]]".format(sys.argv[0]))
    parser.add_argument("--lang",help="Wiktionary dump language to use", default=None, required=True)
    parser.add_argument("--outputdir", help="Path to store the downloaded and decompressed dumps", default=None, required=True)
    parser.add_argument("--targetdir", help="Directory for compiled java", default=None, required=True)
    args = parser.parse_args()

    base_url = "https://dumps.wikimedia.org/{}wiktionary/latest/{}wiktionary-latest-pages-articles-multistream.xml.bz2".format(args.lang,args.lang)
    bz2_file = "{}/{}wiktionary-latest-pages-articles-multistream.xml.bz2".format(args.outputdir,args.lang)
    xml_file = bz2_file[:-len('.bz2')]
    if os.path.isfile(xml_file):
        logger.info("--> "+args.lang+" dump found!")
    else:
        #download the wiktionary dump in bz2
        if not os.path.isfile(bz2_file):
            logger.info("--> downloading "+args.lang+" dump...")
            bz2_file = wget.download(base_url, out=args.outputdir)
        #decompress the wiktionary dump to xml
        logger.info("--> decompressing "+args.lang+" dump...")
        with bz2.BZ2File(bz2_file) as f1, open(xml_file, 'wb') as f2:
            f2.write(f1.read())

    divider = ";" if sys.platform.startswith('win') else ":"
    target = divider.join([os.path.join(args.targetdir, j) for j in os.listdir(args.targetdir) if re.match(r".+\.jar", j)])

    #run java
    env = dict(os.environ)
    java_arguments = ['java', '-Xmx1g','-cp', target, 'com.aegaeon.zamenhof.parser.Generate',xml_file, args.outputdir]
    logger.info("--> Starting JVM...")
    subprocess.call(java_arguments, env=env)

if __name__ == '__main__':
    try:
        main()  # Running main program
    except Exception as ex:
        tb = traceback.format_exc()
        logger.error(tb)
        sys.exit(1)

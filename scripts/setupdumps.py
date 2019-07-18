import argparse
import bz2
import logging
import sys
import traceback

import wget

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger("log")


def main():
    parser = argparse.ArgumentParser(description="Usage: {0} --tempdir dir --lang language".format(sys.argv[0]))
    parser.add_argument("--lang", help="Request dump language", default=None, required=False)
    parser.add_argument("--tempdir", help="Directory to process the dump", default=None, required=True)
    args = parser.parse_args()

    if not args.tempdir:
        logger.error("No temp directory given")
        sys.exit(1)
    lang = "en" if args.lang is None else args.lang
    tempdir = args.tempdir
    base_url = "https://dumps.wikimedia.org/{}wiktionary/latest/{}wiktionary-latest-pages-articles-multistream.xml.bz2"
    logger.info("-->downloading " + lang + " dump")
    dumpfile = wget.download(base_url.format(lang, lang), out=tempdir)
    output_dumpfile = dumpfile[:-len('.bz2')]
    logger.info("-->decompressing " +lang + " dump")
    with bz2.BZ2File(dumpfile) as f1, open(output_dumpfile, 'wb') as f2:
            f2.write(f1.read())
    logger.info("-->complete")


if __name__ == '__main__':
    try:
        main()
    except Exception as ex:
        tb = traceback.format_exc()
        logger.error(tb)
        sys.exit(1)
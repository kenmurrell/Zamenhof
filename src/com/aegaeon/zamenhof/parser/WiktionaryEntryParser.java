package com.aegaeon.zamenhof.parser;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class WiktionaryEntryParser {

    private Logger logger = Logger.getLogger(WiktionaryEntryParser.class.getName());

    private Stack<String> head;

    private Stack<String> body;

    public WiktionaryEntryParser()
    {
        head = new Stack<>();
        body = new Stack<>();
    }


    public void parse(final WiktionaryPage page, String text)
    {
        Iterator<String> lines = Arrays.asList(text.split("\n")).iterator();
        while(lines.hasNext())
        {
            String line = lines.next().trim();
            if(isHead(line))
            {
                int diff;
                if(!head.isEmpty() && (diff=getLevel(head.peek(),0)-getLevel(line,0))>=0)
                {
                    unload(diff+1);
                }
                else if(!lines.hasNext() || line.equals("----"))
                {
                    unload(head.size());
                }
                head.push(line);
            }
            else
            {
                if(!head.empty()) {
                    body.push(line);
                }
            }
        }
    }

    private void unload(int i)
    {

    }

    private int getLevel(String line,int level)
    {
        return line.startsWith("=")? getLevel(line.substring(1),level+1) : level;
    }

    private boolean isHead(String line)
    {
        return line.startsWith("=") && line.endsWith("=");
    }


}

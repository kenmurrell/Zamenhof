package com.aegaeon.zamenhof.parser.utils;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Template {

    private static final Pattern TEMPLATE_BRACKETS = Pattern.compile("(\\{\\{|\\}\\})");

    private List<String> numberedParameters;

    private Map<String,String> labeledParameters;

    private Template()
    {
        numberedParameters = new ArrayList<>();
        labeledParameters = new HashMap<>();
    }

    public static Template create(String text)
    {
        if(!text.startsWith("{{")||!text.endsWith("}}"))
            return null;
        String[] parameters = RegExUtils.removeAll(text,TEMPLATE_BRACKETS).split("\\|");
        if(parameters.length<1)
            return null;
        Template template = new Template();
        for (String parameter : parameters) {
            if (parameter.contains("=")) {
                int p = parameter.indexOf("=");
                String key = parameter.substring(0,p);
                String val = parameter.substring(p+1);
                template.labeledParameters.putIfAbsent(key,val);
            } else {
                template.numberedParameters.add(parameter);
            }
        }
        return template;
    }

    public static List<Template> createAll(String text)
    {
        List<String> headers = new ArrayList<>();
        aggregateTemplates(text, headers);
        return headers.stream()
                .map(Template::create)
                .collect(Collectors.toList());
    }

    private static void aggregateTemplates(String text, List<String> headers) {
        //old legacy thing i wrote, this should even capture templates inside other templates
        int startidx = text.indexOf("{{");
        if(startidx>=0)
        {
            //TODO:replace the replace() method with a more robust system
            String subtext = text.substring(startidx+2);
            aggregateTemplates(subtext,headers);
            for(String header : headers)
            {
                text = StringUtils.replace(text,header,"<TEMPLATE>");
            }
        }
        int endidx  = text.indexOf("}}");
        if(endidx >=0)
        {
            String temp = text.substring(0,endidx);
            headers.add("{{"+temp+"}}");
        }
    }

    public Optional<String> getNumberedParameter(int num)
    {
        if(num<this.numberedParameters.size())
        {
            return Optional.of(this.numberedParameters.get(num));
        }
        return Optional.empty();
    }

    public int numberedParameterCount()
    {
        return this.numberedParameters.size();
    }

    public Optional<String> getLabelledParameter(String name)
    {
        if(labeledParameters.containsKey(name))
        {
            return Optional.of(labeledParameters.get(name));
        }
        return Optional.empty();
    }

    public int labelledParameterCount()
    {
        return this.labeledParameters.size();
    }
}

package com.aegaeon.zamenhof.parser.utils;

import java.util.*;
import java.util.stream.Collectors;

public class Template {

    private List<String> numberedParameters;

    private Map<String,String> labeledParameters;

    public Template()
    {
        numberedParameters = new ArrayList<>();
        labeledParameters = new HashMap<>();
    }

    public static Template create(String text)
    {
        if(!text.startsWith("{{")||!text.endsWith("}}"))
            return null;
        String[] parameters = text.replaceAll("(\\{\\{|\\}\\})","").split("\\|");
        if(parameters.length<1)
            return null;
        Template template = new Template();
        for (String parameter : parameters) {
            if (parameter.contains("=")) {
                int p = parameter.indexOf("=");
                String key = parameter.substring(0,p);
                String val = parameter.substring(p+1);
                template.addLabeledParameter(key, val);
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
            String subtext = text.substring(startidx+2);
            aggregateTemplates(subtext,headers);
            for(String header : headers)
            {
                text = text.replace(header,"<TEMPLATE>");
            }
        }
        int endidx  = text.indexOf("}}");
        if(endidx >=0)
        {
            String temp = text.substring(0,endidx);
            headers.add("{{"+temp+"}}");
        }
    }

    public String getNumberedParameter(int i)
    {
        if(i<this.numberedParameters.size())
        {
            return this.numberedParameters.get(i);
        }
        else
        {
            return null;
        }
    }

    public void addNumberedParameter(String value)
    {
        this.numberedParameters.add(value);
    }

    public String getLabeledParameter(String label)
    {
        return this.labeledParameters.getOrDefault(label, null);
    }

    public void addLabeledParameter(String key, String value)
    {
        this.labeledParameters.putIfAbsent(key,value);
    }

    public int numberedParameterCount()
    {
        return this.numberedParameters.size();
    }
}

package com.company.services;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class TemplateResolverImpl implements TemplateResolver {
    @Autowired
    @Qualifier(value = "freeMarkerConfiguration")
    private Configuration configuration;

    @Override
    public void process(String name, Map<String, String> root, Writer writer) {
        try {
            Template template = configuration.getTemplate(name);
            template.process(root, writer);
        } catch (IOException | TemplateException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String process(String name, Map<String, String> root) {
        try {
            Template template = configuration.getTemplate(name);
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, root);
        } catch (IOException | TemplateException e) {
            throw new IllegalStateException(e);
        }
    }
}

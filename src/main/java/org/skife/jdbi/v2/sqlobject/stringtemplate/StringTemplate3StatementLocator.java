package org.skife.jdbi.v2.sqlobject.stringtemplate;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.stringtemplate.language.AngleBracketTemplateLexer;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.StatementLocator;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class StringTemplate3StatementLocator implements StatementLocator
{
    private final StringTemplateGroup group;

    public StringTemplate3StatementLocator(String templateGroupFilePathOnClasspath)
    {
        InputStream ins = getClass().getResourceAsStream(templateGroupFilePathOnClasspath);
        InputStreamReader reader = new InputStreamReader(ins);
        try {
            this.group = new StringTemplateGroup(reader, AngleBracketTemplateLexer.class);
            reader.close();
        }
        catch (IOException e) {
            throw new IllegalStateException("unable to load string template group " + templateGroupFilePathOnClasspath,
                                            e);
        }
    }

    public String locate(String name, StatementContext ctx) throws Exception
    {
        if (group.isDefined(name)) {
            StringTemplate t = group.lookupTemplate(name);
            for (Map.Entry<String, Object> entry : ctx.getAttributes().entrySet()) {
                t.setAttribute(entry.getKey(), entry.getValue());
            }
            return t.toString();
        }
        else {
            // no template matches name, so just return it
            return name;
        }
    }
}

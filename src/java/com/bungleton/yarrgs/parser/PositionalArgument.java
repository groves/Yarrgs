package com.bungleton.yarrgs.parser;

import java.lang.reflect.Field;

import com.bungleton.yarrgs.Parser;
import com.bungleton.yarrgs.Positional;

public class PositionalArgument extends Argument
{
    public final boolean optional;

    public final Parser<?> parser;

    public PositionalArgument (Field field, Parser<?> parser)
    {
        super(field);
        optional = field.getAnnotation(Positional.class).optional();
        this.parser = parser;
    }

    @Override
    public String getBasic ()
    {
        return optional ? "[" + field.getName() + "]" : field.getName();
    }

    @Override
    public String getDetail ()
    {
        return field.getName() + " " + getUsage();
    }
}

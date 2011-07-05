//
// Yarrgs - Java command line argument parsing with a hint of a sea breeze
// http://github.com/groves/yarrgs

package com.bungleton.yarrgs;

import org.junit.Test;

import com.bungleton.yarrgs.parser.Command;
import com.bungleton.yarrgs.parser.Parsers;

import static org.junit.Assert.assertNull;

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertTrue;

public class TestCommand
{
    @Test(expected = YarrgConfigurationException.class)
    public void parseMalformedUnmatched ()
    {
        new Command<NonparameterizedUnmatched>(NonparameterizedUnmatched.class,
                Parsers.createFieldParserFactory());
    }

    @Test(expected = YarrgConfigurationException.class)
    public void parseMissingUnmatchedParser ()
    {
        new Command<EnumUnmatched>(EnumUnmatched.class, Parsers.INT);
    }

    @Test
    public void parseMashedShortargs ()
        throws YarrgParseException
    {
        Complete complete = parseWithComplete("-lv", "filename");
        assertTrue(complete.verbose);
        assertTrue(complete.longFormat);
    }

    @Test(expected = YarrgHelpException.class)
    public void parseHelpFromMashedShortargs ()
        throws YarrgParseException
    {
        parseWithComplete("-lhv", "file");
    }

    @Test(expected = YarrgParseException.class)
    public void parseValueAmidstMashedShortargs ()
        throws YarrgParseException
    {
        parseWithComplete("-lsv", "2009-10-03", "file");
    }

    @Test
    public void parseValueAtTheEndOfMashedShortargs ()
        throws YarrgParseException
    {
        Complete complete = parseWithComplete("-lvs", "2009-10-03", "filename");
        assertTrue(complete.verbose);
        assertTrue(complete.longFormat);
        assertNull(complete.injuries);
    }

    @Test
    public void parseStdinFilename ()
        throws YarrgParseException
    {
        assertEquals("-", parseWithComplete("-").file);
    }

    @Test(expected = YarrgParseException.class)
    public void parseMissingPositional ()
        throws YarrgParseException
    {
        parseWithComplete("-lv");
    }

    @Test(expected = YarrgParseException.class)
    public void parseDoubleFlag ()
        throws YarrgParseException
    {
        parseWithComplete("-ll", "filename");
    }

    @Test
    public void parseMultioption ()
        throws YarrgParseException
    {
        Complete complete = parseWithComplete("-lihook", "-vi", "pegleg", "filename");
        assertEquals(2, complete.injuries.size());
        assertEquals(Injury.hook, complete.injuries.get(0));
        assertEquals(Injury.pegleg, complete.injuries.get(1));
        assertTrue(complete.verbose);
    }

    @Test
    public void parseMapoption ()
        throws YarrgParseException
    {
        Complete complete = parseWithComplete("-ldcaptain=4", "-d", "swabbie=1", "filename");
        assertEquals(2, complete.divisions.size());
        assertEquals(4, (int)complete.divisions.get("captain"));
        assertEquals(1, (int)complete.divisions.get("swabbie"));
        assertTrue(complete.longFormat);
    }

    @Test(expected = YarrgParseException.class)
    public void parseMissingMapValue ()
        throws YarrgParseException
    {
        parseWithComplete("-ld", "captain", "-d", "swabbie=1", "filename");
    }

    @Test(expected = YarrgParseException.class)
    public void parseUnknownFlag ()
        throws YarrgParseException
    {
        parseWithComplete("-q");
    }

    protected static Complete parseWithComplete (String... args)
        throws YarrgParseException
    {
        return new Command<Complete>(Complete.class,
                Parsers.createFieldParserFactory()).parse(args);
    }
}

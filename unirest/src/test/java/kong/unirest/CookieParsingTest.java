/**
 * The MIT License
 *
 * Copyright for portions of unirest-java are held by Kong Inc (c) 2013.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package kong.unirest;

import org.junit.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class CookieParsingTest {

    @Test
    public void parseFull() {
        Cookie c = new Cookie("color=blue;Path=/get;Domain=localhost;Expires=Sun, 05-Jan-2020 15:00:20 GMT;Max-Age=42;HttpOnly");
        assertEquals("blue", c.getValue());
        assertEquals("localhost", c.getDomain());
        assertEquals("/get", c.getPath());
        assertEquals(ZonedDateTime.of(2020,1,5,15,0,20, 0, ZoneId.of("GMT")),
                c.getExpiration());
        assertTrue(c.isHttpOnly());
        assertFalse(c.isSecure());
        assertEquals(42, c.getMaxAge());
        assertNull(c.getSameSite());
    }

    @Test
    public void alternateDate() {
        Cookie c = new Cookie("color=blue;Path=/get;Domain=localhost;Expires=Sun, 05 Jan 2020 15:00:20 GMT;Max-Age=42;HttpOnly");
        assertEquals(ZonedDateTime.of(2020,1,5,15,0,20, 0, ZoneId.of("GMT")),
                c.getExpiration());
    }

    @Test
    public void parseBackOutToString() {
        String v = "color=blue;Path=/get;Domain=localhost;Expires=Sun, 05-Jan-2020 15:00:20 GMT;Max-Age=42;HttpOnly";
        Cookie c = new Cookie(v);
        assertEquals(v, c.toString());
    }

    @Test
    public void sameSite() {
        String v = "color=blue;SameSite=Strict";
        Cookie c = new Cookie(v);
        assertEquals(Cookie.SameSite.Strict, c.getSameSite());
    }

    @Test
    public void secure() {
        String v = "color=blue;Secure";
        Cookie c = new Cookie(v);
        assertEquals(true, c.isSecure());
        assertEquals("color=blue;Secure", c.toString());
    }

    @Test
    public void futurePropsDontBreak() {
        String v = "color=blue;TheFuture";
        Cookie c = new Cookie(v);
        assertEquals("color=blue", c.toString());
    }

    @Test
    public void emptyValue() {
        String v = "SignOnDefault=; domain=.admin.virginia.edu; path=/; HttpOnly";
        Cookie c = new Cookie(v);
        assertEquals("", c.getValue());
        assertTrue(c.isHttpOnly());
        assertEquals(".admin.virginia.edu", c.getDomain());
    }

    @Test
    public void theValieCanBeDoubleQuoted() {
        String v = "SignOnDefault=\" woh \";";
        Cookie c = new Cookie(v);
        assertEquals(" woh ", c.getValue());
    }

    @Test
    public void justEmptyQuotes() {
        String v = "SignOnDefault=\"\";";
        Cookie c = new Cookie(v);
        assertEquals("", c.getValue());
    }

    @Test
    public void justOneQuote() {
        String v = "SignOnDefault=\";";
        Cookie c = new Cookie(v);
        assertEquals("\"", c.getValue());
    }

    @Test
    public void justOneSideOfquotes() {
        String v = "SignOnDefault=\"foo;";
        Cookie c = new Cookie(v);
        assertEquals("\"foo", c.getValue());
    }
}

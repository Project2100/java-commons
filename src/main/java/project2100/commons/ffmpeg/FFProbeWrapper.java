/*
 * MIT License
 *
 * Copyright (c) 2018 Andrea Proietto
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package project2100.commons.ffmpeg;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author Project2100
 */
public class FFProbeWrapper {
    
    public static final String ffprobeMissingMessage = "FFProbe not found!";
    
    // If ffprobe is missing, the generated IOException will be set as cause to
    // a new IOE with message <code>ffprobemissingmessage<\code>
    // WARNING ONLY ONE-LINERS allowed here
    private static String executeCommand(String[] command) throws IOException {
        
        try {
            Process ffprobe = Runtime.getRuntime().exec(command);
            //int exitCode=ffprobe.waitFor();
            return new BufferedReader(new InputStreamReader(ffprobe.getInputStream())).readLine();
        }
        catch (IOException ex) {
            if (ex.getMessage().contains("CreateProcess error=2")) throw new IOException(ffprobeMissingMessage, ex);
            else throw ex;
        }
    }
    
    
    /**
     * Returns an integer representing the duration of the video in seconds,
     * rounded up.
     *
     * @param source The video file
     * @return The duration of the video
     * @throws java.io.IOException if FFProbe is missing 
     * (grep for ffprobeMissingMessage) 
     * or some other IO failure occurs
     */
    public static int getVideoLength(Path source) throws IOException {

        return Integer.parseInt(
                executeCommand(new String[] {
                        "ffprobe",
                        "-show_entries", // entry filter
                        "format=duration", //  ""
                        "-print_format", // how ffprobe displays output
                        "flat", //  ""
                        source.toAbsolutePath().toString()
                }).split("\"|\\.")[2] //extract value
        ) + 1; // round up
    }
}

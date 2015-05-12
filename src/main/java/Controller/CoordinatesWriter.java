/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

/*
 Copyright 2008-2013 Clement Levallois
 Authors : Clement Levallois <clementlevallois@gmail.com>
 Website : http://www.clementlevallois.net


 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

 Copyright 2013 Clement Levallois. All rights reserved.

 The contents of this file are subject to the terms of either the GNU
 General Public License Version 3 only ("GPL") or the Common
 Development and Distribution License("CDDL") (collectively, the
 "License"). You may not use this file except in compliance with the
 License. You can obtain a copy of the License at
 http://gephi.org/about/legal/license-notice/
 or /cddl-1.0.txt and /gpl-3.0.txt. See the License for the
 specific language governing permissions and limitations under the
 License.  When distributing the software, include this License Header
 Notice in each file and include the License files at
 /cddl-1.0.txt and /gpl-3.0.txt. If applicable, add the following below the
 License Header, with the fields enclosed by brackets [] replaced by
 your own identifying information:
 "Portions Copyrighted [year] [name of copyright owner]"

 If you wish your version of this file to be governed by only the CDDL
 or only the GPL Version 3, indicate your decision by adding
 "[Contributor] elects to include this software in this distribution
 under the [CDDL or GPL Version 3] license." If you do not indicate a
 single choice of license, a recipient has the option to distribute
 your version of this file under either the CDDL, the GPL Version 3 or
 to extend the choice of license to its licensees as provided above.
 However, if you add GPL Version 3 code and therefore, elected the GPL
 Version 3 license, then the option applies only if the new code is
 made subject to such option by the copyright holder.

 Contributor(s): Clement Levallois

 */
class CoordinatesWriter {

    void write(double precision) throws FileNotFoundException, IOException {
        BufferedReader br;

        File wd = new File(Main.filepath);
        Scanner sc1;
        String pattern = ", ";
        Pattern p = Pattern.compile(pattern);
        StringBuilder sb = new StringBuilder();
        String[] latlong;
        int nodeCount = 0;
        double lastLat = 0d;
        double lastLong = 0d;

        for (File file : wd.listFiles()) {
            if (!file.getName().endsWith("txt")) {
                continue;
            }
            System.out.println("file: " + file.getName());

            br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
//            System.out.println(line);
            br.close();
            sc1 = new Scanner(line);
            sc1.useDelimiter(p);
            sb.append("country:Australia	region:999	subregion:999	district:").append(file.getName().replace(".txt", "")).append("	");
            boolean newShape = true;
            boolean endShape = false;
            String regularString;

            while (sc1.hasNext()) {

                regularString = sc1.next();

                if (regularString.startsWith("((") || regularString.startsWith("(")) {
                    regularString = regularString.replace("((", "");
                    regularString = regularString.replace("(", "");
                    newShape = true;
                    endShape= false;
                } else if (regularString.endsWith("))") || regularString.endsWith(")")) {
                    regularString = regularString.replace("))", "");
                    regularString = regularString.replace(")", "");
                    endShape = true;
                }

                latlong = regularString.split(" ");

                if (!endShape) {
                    if (Math.abs(Double.valueOf(latlong[0]) - lastLat) < precision) {
                        if (Math.abs(Double.valueOf(latlong[1]) - lastLong) < precision) {
                            continue;
                        }
                    }
                }

                sb.append("node");
                sb.append(nodeCount);
                sb.append(":");
                sb.append(latlong[0]);
                sb.append(" ");
                sb.append(latlong[1]);
                sb.append("\t");
                lastLat = Double.valueOf(latlong[0]);
                lastLong = Double.valueOf(latlong[1]);

                if (!newShape) {
                    sb.append("edge:");
                    sb.append(nodeCount);
                    sb.append(" ");
                    sb.append(nodeCount - 1);
                    sb.append("\t");
                }
                if (newShape) {
                    newShape = false;
                }
                nodeCount++;

            }
            sb.append("\n");
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(Main.filepath + "Australian states.csv"));
        bw.write(sb.toString());
        bw.close();
        System.out.println("node count: " + nodeCount);
    }
}

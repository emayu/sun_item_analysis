/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package gt.edu.usac.sun.item_analysis_tool;

import gt.edu.usac.sun.item_analysis_tool.analysis.Analizer;
import gt.edu.usac.sun.item_analysis_tool.model.TestProccedData;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 *
 * @author emayu
 */
public class Sun_item_analysis_tool {

    private static final Logger LOG = Logger.getLogger(Sun_item_analysis_tool.class.getName());
    
    

    public static void main(String[] args) throws FileNotFoundException, IOException {
        //check input params
        Path path = null;
        File file;
        LOG.info(()-> "Args size " + args.length);
        if(args.length == 1){
            path = Paths.get(args[0]);
            if(!Files.exists(path)){
                LOG.severe("El archivo especificado no existe");
                System.exit(-1);
            }
            file = path.toFile();
            Analizer analizer = new Analizer();
            TestProccedData data = analizer.analyze(file);
            analizer.printStatic(data, System.out);
        } else {
            LOG.info("Analyzing from hard code data");
            Analizer analizer = new Analizer();
            TestProccedData data = analizer.analyze(TEST_DATA, "memory data Q507_38items");
            analizer.printStatic(data, System.out);
        }
        
    }
    
    private static final String TEST_DATA = """
                                                40 Z O 0
                                               BCDCBAACDBABACBCABABADABAADBCBACAABBCCCB
                                               4444444444444444444444444444444444444444
                                               YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYNNYYYYYYYY
                                               BCCDBCBACDDACBBACAACADABBBDBDBCBAAACABAD
                                               BCDCBDDDCDBBDCACAABCBABBABBACDDBAAABCADA
                                               ACBCBDCADBCACDACBAAAAAACDBBADBDCDAADAAAD
                                               DCCCBACCCDBADDDDCDCBDDBDAAACBAABADCBCABD
                                               CCDBBABACBAADBAACBBBCAACDAACABDACACACDAD
                                               CCBDBAAABBBBBBBCDABCAAACDBBDABCCAAACDCCC
                                               ACDBBDACCDBCDCCCCDDDACZADDCCBBACACABCCAD
                                               CABAAABACBBABDDCCABCACAAADCCBCCACAABBAAA
                                               CCDDABDCCDABCBCCADBBADCCABDBDDCABAADDCDB
                                               DCDBCBACCBCAABDCBADCADABABAADDAAAABBCADD
                                               BCDBCBDCCBDAADACBAACDDBCDBADDDZCBACDACDB
                                               BCDBBAAADDBBDCBABADCADACABADABDAAABDCCAB
                                               CCDDBADACBDBACACCDABAAACBACCBDAABACADABB
                                               BCDDBCADCDCBDDACBADAADACDDDADBAAAAACCABB
                                               BBDBBABCCBABADBCCAABADACDDAADBBAAAACCBBB
                                               CCDDADCBCBACDDBACAACADAADABBABAABACACACB
                                               BCDDBAACCBDDBBBCAAAAACACADABDBBABACDCACB
                                               CCDDAABACAAABBBCCDBAAAABADDBABACABCCAADD
                                               CCDDBABACDDADDDCCBBABCBBBADADDAAAACCBADC
                                               CCCCADCDCBABADACCCBBCCAADCDADBBADACCCAAD
                                               CBDDBACACBBACDCCCACCADBBBBCADBCCCAAADAAB
                                               BCCCBCBCCADBCDAABDABAAACAACAABCCBACAAADD
                                               CCDABACCCDDCCBDCCAAAACABDBABABCCAAACBCAB
                                               DCDDBDCACADBBDACAABCAAACBBCCCBACAAABDCCD
                                               BCDDADCCDBDAABBDADAAADABAAABCBCAAAAACCCC
                                               CCDCBADCCDBDDBBCCACCBDBCDAADABDBBAAACAAD
                                               DCDDADCCADBCBCACAABBBDBBAAADABAABACDCDAC
                                               CCBCABDDDDAADBBCCABBACABADAACACBAAAADCCB
                                               BCDDBDCCDBAAADACAABDAAACAAABDBADAAABCADB
                                               ZCDDBDACCBCBABBCABBAADCCDADBDBAAABBBACCC
                                               BCDCBDDCDDABBCBCADBBADBBBCCADBCAAAABCADA
                                               CCDDADADCCDADCBCBDDBADABBBABCACABAADCCCB
                                               BCDDBDCCCDADAADCABBCADAABADBABDAAABBCCCB
                                               BCBBBDCABAAABCBCBBDAAAACDBCDCCAAAABACCDB
                                               CDDBBCADCBBACDAABCBCACZBABDACBCDAAAACADB
                                               BCDDBDADCBBBABBCBBBAADAAAAABABCAAACBCCCB
                                               CCCBBACAABBBCBCCCABBABBCDCABDBCDCDBCDCAD
                                               CCDABADCCBAABDACAAABACABCDBDDAAAAAACAACC
                                               CDDCACDDCCDABDACCABCACBCZCBABBAADACCDADD
                                               ACDABBACCBBBADBCCDABACBCBCADABCCAADBCBCD
                                               CCDBBDCDDDCABDCBBDDAAABBDDCCDACCBAAACCAD
                                               CDDAADBDDBADADDCBBCCACACBAAADCDBAAAADACB
                                               BCDDBDACCDDBBDDADCCAADBCDDCBCBACDAACBDBC
                                               CDADACCACADAADDCCDBCCABCDBCDDDBABCCCABBC
                                               ACCBBBCCDABCAADCAACDBCBAADDBBAABAABAABDC
                                               ABCDADAACDACADBBABABACABDADDBBAABAABCDCD
                                               BCDAAABACDBAABBDBBCBADACBCCAAAAAAABBCABB
                                               CCBABDABABAADDACACBBACBBADCCDDBCCAACBDAC
                                               CCDBBDCDCDDADDDABDCBAACACABADCDBBDBDDAAD
                                               ACDDABAACDAAADDCCBADBAACBAADCADCAAZABDAD
                                               BCDABBACDBCBADBBADBBADACBAACDBCDCBDACABB
                                               CDCCBDCBCABACBBAADABDDABBADBDBCBCABCBACD
                                               BCDCBDDCCDACDDBCCCBACAABBDACDBDCAAADCABA
                                               DABCBADCCABAADCCCABBCCBCDDAADBDBCCBBADDD
                                               BCDCBDCDCDABBDACCACCADACDAACBACCAAAACCBD
                                               BCDDADACCDDAACBCABABAABCBCABCBAAAAABCCCA""";
}

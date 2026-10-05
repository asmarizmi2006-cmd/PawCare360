import java.io.File;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;

// Compile jrxml to jasper
public class CompileReports
{
    public static void main(String[] args) throws Exception
    {
        String dir = args.length > 0 ? args[0] : "src/reports";
        DefaultJasperReportsContext.getInstance().setProperty(
                "net.sf.jasperreports.compiler.class", "net.sf.jasperreports.engine.design.JRJdk13Compiler");
        File[] files = new File(dir).listFiles((d, n) -> n.endsWith(".jrxml"));
        if (files == null) { System.out.println("No folder: " + dir); return; }
        for (File f : files)
        {
            String out = f.getPath().replace(".jrxml", ".jasper");
            JasperCompileManager.compileReportToFile(f.getPath(), out);
            System.out.println("Compiled " + out);
        }
    }
}

package cz.dangelcz.print.grfgen.test;

import cz.dangelcz.print.grfgen.libs.IoHelper;
import cz.dangelcz.print.grfgen.logic.GrfReader;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Scanner;

public class GrfReadingTest
{
    public static void main(String[] args) throws IOException
    {
        //System.out.println("Press enter to start ...");
        //new Scanner(System.in).nextLine();


        //BufferedImage testImage = GrfReader.readGrf("data/tst1.grf");
        //IoHelper.saveImage(testImage, "data/new_tst1.png", true);

        BufferedImage testImage = GrfReader.readGrf("data/tst2.grf");
        IoHelper.saveImage(testImage, "data/new_tst2.png", true);

        //BufferedImage testImage = GrfReader.readGrf("data/tst3.grf");
        //IoHelper.saveImage(testImage, "data/new_tst3.png", true);
    }
}

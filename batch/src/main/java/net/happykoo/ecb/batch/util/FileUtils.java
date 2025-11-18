package net.happykoo.ecb.batch.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileUtils {

  public static List<File> splitCsv(File csvFile, int gridSize) {
    try (Stream<String> stream = Files.lines(csvFile.toPath(), StandardCharsets.UTF_8)) {
      long lineCount = stream.count();
      long linesPerFile = (long) Math.ceil(lineCount / (double) gridSize);

      return splitCsvByLinesPerFile(csvFile, linesPerFile);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static List<File> splitCsvByLinesPerFile(File csvFile, long linesPerFile) {
    List<File> splitFiles = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
      String line;
      boolean isFirstLine = true;
      boolean shouldCreateFile = true;
      BufferedWriter writer = null;
      File splitFile;

      int fileIndex = 0;
      int lineCount = 0;
      while ((line = reader.readLine()) != null) {
        if (isFirstLine) {
          isFirstLine = false;
          continue;
        }
        if (shouldCreateFile) {
          splitFile = createTempFile("split_" + (fileIndex++) + "_", ".csv");
          writer = new BufferedWriter(new FileWriter(splitFile));
          splitFiles.add(splitFile);
          lineCount = 0;
          shouldCreateFile = false;
        }
        writer.write(line);
        writer.newLine();
        lineCount++;

        if (lineCount >= linesPerFile) {
          writer.close();
          shouldCreateFile = true;
        }
      }
      if (writer != null) {
        writer.close();
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return splitFiles;
  }

  public static File createTempFile(String prefix, String suffix) throws IOException {
    File tempFile = File.createTempFile(prefix, suffix);
    tempFile.deleteOnExit();

    return tempFile;
  }

  public static void mergeFile(String header, List<File> files, File outputFile) {
    try (BufferedOutputStream outputStream = new BufferedOutputStream(
        new FileOutputStream(outputFile))) {
      outputStream.write((header + "\n").getBytes(StandardCharsets.UTF_8));
      for (File file : files) {
        Files.copy(file.toPath(), outputStream);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}

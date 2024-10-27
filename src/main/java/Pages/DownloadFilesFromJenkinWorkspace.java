package Pages;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DownloadFilesFromJenkinWorkspace {
	public static String  downloadPath = System.getenv("WORKSPACE") + "/downloads";

	public static void downloadFileFromJenkins() {
		try {
			// Source file in Jenkins workspace
			Path sourceFilePath = Paths.get(downloadPath, "5741603971488.pdf");
			System.out.println("sourcepath: "+ sourceFilePath);

			// Destination file in local directory
			Path destinationFilePath = Paths.get("C:/Downloads/", sourceFilePath.getFileName().toString());

			// Create directories if they do not exist
			Files.createDirectories(destinationFilePath.getParent());

			// Copy file from Jenkins workspace to local directory
			Files.copy(sourceFilePath, new FileOutputStream(destinationFilePath.toFile()));
			System.out.println("File downloaded successfully to " + destinationFilePath);

		} catch (IOException e) {
			System.out.println("An error occurred while downloading the file: " + e.getMessage());
			e.printStackTrace();
		}
	}

}

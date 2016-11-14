package eu.motogymkhana.competition.util;

import android.content.Context;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FileAssetManagerImpl implements FileAssetManager {

	@Inject
	protected Context context;

	@Override
	public String getFileContent(String name) throws IOException {

		return FileUtils.readFile(context, name);
	}

}

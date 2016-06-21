package eu.motogymkhana.competition.util;

import android.content.Context;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.IOException;

import roboguice.inject.ContextSingleton;

@Singleton
public class FileAssetManagerImpl implements FileAssetManager {

	@Inject
	private Context context;

	@Override
	public String getFileContent(String name) throws IOException {

		return FileUtils.readFile(context, name);
	}

}

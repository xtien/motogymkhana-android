package eu.motogymkhana.competition.context.impl;

import android.content.Context;

import com.google.inject.Inject;

import eu.motogymkhana.competition.context.ContextProvider;

public class ContextProviderImpl extends ContextProvider {

	@Inject
	public ContextProviderImpl(Context ctx) {
		context = ctx;
	}
}

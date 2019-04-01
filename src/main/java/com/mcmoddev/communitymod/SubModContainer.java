package com.mcmoddev.communitymod;

public class SubModContainer {

	private final String name;
	
	private final String description;
	
	private final String attribution;
	
	private final ISubMod subMod;

	private final String id;
	
	public SubModContainer(String name, String description, String attribution, ISubMod subMod) {
		
		this.name = name;
		this.description = description;
		this.attribution = attribution;
		this.subMod = subMod;
		this.id = name.replaceAll(" ","_").replaceAll("(\\\\W|^_)*", "").toLowerCase();
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getAttribution() {
		return attribution;
	}

	public ISubMod getSubMod() {
		return subMod;
	}
	
	public String getId() {
		
		return id;
	}
}
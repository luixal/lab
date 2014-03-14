package es.luixal.ragbag;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class Tag implements Parcelable {

	private String name;
	private String uuid;
	private Location location;
	
	
	public Tag() {
		super();
		this.name = "";
		this.uuid = "";
		this.location = null;
	}

	public Tag(String name, String uuid, Location location) {
		super();
		this.name = name;
		this.uuid = uuid;
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "Tag [name=" + name + ", uuid=" + uuid + ", location="
				+ location + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Tag == false) return false;
		return ((Tag)obj).getUuid().equalsIgnoreCase(this.getUuid());
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.name);
		dest.writeString(this.uuid);
	}
	
}

package es.luixal.ragbag;

import java.util.List;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

@Table(name = "Tags")
public class Tag extends Model implements Parcelable {

	@Column(name = "name")
	private String name;
	@Column(name = "uuid", index = true)
	private String uuid;
	@Column(name = "latitude")
	private double latitude;
	@Column(name = "longitude")
	private double longitude;
	
	
	public Tag() {
		super();
		this.name = "";
		this.uuid = "";
		this.latitude = 0;
		this.longitude = 0;
	}

	public Tag(String name, String uuid, Location location) {
		super();
		this.name = name;
		this.uuid = uuid;
		this.latitude = location.getLatitude();
		this.longitude = location.getLongitude();
	}
	
	public Tag(String name, String uuid, double latitude, double longitude) {
		super();
		this.name = name;
		this.uuid = uuid;
		this.latitude = latitude;
		this.longitude = longitude;
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

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public boolean isLocated() {
		return (this.latitude != 0 || this.longitude != 0);
	}

	@Override
	public String toString() {
		return "Tag [name=" + name + ", uuid=" + uuid + ", latitude="
				+ latitude + ", longitude=" + longitude + "]";
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
	
	public static List<Tag> getAll() {
		return new Select().from(Tag.class).execute();
	}
	
	public static void removeAll() {
		new Delete().from(Tag.class).execute();
	}
	
}

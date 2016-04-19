/**
 * @author xingyuchen
 * Created on Mar 17, 2016
 */
package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author xingyuchen
 *
 */
@Entity
public class Author {
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
	private String authorName;
	private String institute;
	
	public Author() {
		super();
	}

	public Author(String authorName, String institute) {
		super();
		this.authorName = authorName;
		this.institute = institute;
	}



	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getInstitute() {
		return institute;
	}

	public void setInstitute(String institute) {
		this.institute = institute;
	}

	@Override
	public String toString() {
		return "Author [id=" + id + ", authorName=" + authorName + ", institute=" + institute
				+ "]";
	}
	
	
	
}

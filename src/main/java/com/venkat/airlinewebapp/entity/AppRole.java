package com.venkat.airlinewebapp.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity(name = "app_role")
public class AppRole implements Serializable {

	private static final long serialVersionUID = 5191365184969566982L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String name;

	@ManyToMany(mappedBy = "roles")
	private Collection<AppUser> users;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "roles_authorities", 
			joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"), 
			inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
	private Set<AppAuthority> authorities;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<AppUser> getUsers() {
		return users;
	}

	public void setUsers(Collection<AppUser> users) {
		this.users = users;
	}

	public Set<AppAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<AppAuthority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AppRole other = (AppRole) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}

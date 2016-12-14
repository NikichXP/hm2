package com.hm.entity;

import lombok.*;

import java.util.Arrays;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class Moderator extends User {

	private int accessLevel;

	public Moderator(User user) {
		Arrays.asList(user.getClass().getMethods()).stream()
				.filter(method -> method.getName().startsWith("get"))
				.forEach(usermeth -> {
					String name = usermeth.getName().substring(3);
					Arrays.asList(this.getClass().getMethods()).stream()
							.filter(meth -> meth.getName().startsWith("set"))
							.filter(el -> el.getName().substring(3).equals(name))
							.findAny().
							ifPresent(e -> {
								try {
									e.invoke(this, usermeth.invoke(user));
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							});
				});
		this.accessLevel = 0;
		this.setEntityClassName("Moderator");
		user.setEntityClassName("Moderator");
	}
}

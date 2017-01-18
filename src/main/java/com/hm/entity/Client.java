package com.hm.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Arrays;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class Client extends User {
	public Client(User user) {
		Arrays.stream(user.getClass().getMethods())
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
		this.setEntityClassName("Client");
		user.setEntityClassName("Client");
	}
}

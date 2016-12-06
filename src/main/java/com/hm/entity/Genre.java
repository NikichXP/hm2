package com.hm.entity;

import lombok.*;
import java.util.*;

@Data
public class Genre {

	private String name;
	private HashMap<ServiceExecutor, Product> users;

}

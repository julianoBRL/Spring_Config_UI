package io.github.julianobrl.configui.dto;

import lombok.Data;

@Data
public class Signin {
	private String username;
	private String password;
	private boolean remeberMe;
}

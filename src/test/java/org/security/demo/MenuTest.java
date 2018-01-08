package org.security.demo;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.security.Application;
import org.security.domain.Menu;
import org.security.domain.MenuRepository;
import org.security.domain.Role;
import org.security.domain.RoleRepository;
import org.security.init.MenuInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class MenuTest {

	private static final Logger log = LoggerFactory.getLogger(MenuTest.class);

	@Inject
	private MenuRepository menuRepository;

	@Inject
	private RoleRepository roleRepository;

	// 初始化所有菜单，并授权给ROLE_ADMIN
	@Test
	public void initData() throws InterruptedException {
		// menuRepository.deleteAll();
		log.info("Start---");
		Menu menu = null;
		for (MenuInfo info : MenuInfo.values()) {
			if (menuRepository.findByMark(info.getMark()) == null) {
				menu = new Menu();
				menu.setMark(info.getMark());
				menu.setMenuName(info.getMenuName());
				menu.setMenuIndex(info.getMenuIndex());
				menu.setMenuUrl(info.getMenuUrl());

				if (info.getMark() != null) {
					Menu parent = menuRepository.findByMark(info.getParent_id());
					if (parent != null) {
						menu.setParent(parent);
					}else{
						menu.setParent(null);
					}
				}
				log.info("insert start " + info.getMenuName() + " to menu");
				menuRepository.save(menu);
				log.info("insert end" + info.getMenuName() + " to menu");
			}

			Menu menuN = menuRepository.findByMark(info.getMark());
			Role roleAdmin = roleRepository.findByRoleNameAndFlag("ROLE_ADMIN", 1);

			if (!menuN.getRoles().contains(roleAdmin)) {
				menuN.getRoles().add(roleAdmin);
				menu = menuRepository.saveAndFlush(menuN);
			}
		}

	}

}

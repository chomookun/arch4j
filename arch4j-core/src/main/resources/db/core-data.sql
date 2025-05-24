-- core_authority
insert into `core_authority`
    (`authority_id`,`system_required`,`name`)
values
    ('admin','Y','Admin Access Authority'),
    ('admin.common','Y','Admin Common Access Authority'),
    ('admin.monitor','Y','Admin Monitor Access Authority'),
    ('admin.user','Y','Admin User Access Authority'),
    ('admin.user:edit','Y','Admin User Edit Authority'),
    ('admin.security','Y','Admin Security Access Authority'),
    ('admin.security:edit','Y','Admin Security Edit Authority'),
    ('admin.menu','Y','Admin Menu Access Authority'),
    ('admin.menu:edit','Y','Admin Menus Edit Authority'),
    ('admin.message','Y','Admin Message Access Authority'),
    ('admin.message:edit','Y','Admin Message Edit Authority'),
    ('admin.variable','Y','Admin Variable Access Authority'),
    ('admin.variable:edit','Y','Admin Variable Edit Authority'),
    ('admin.code','Y','Admin Code Access Authority'),
    ('admin.code:edit','Y','Admin Code Edit Authority'),
    ('admin.execution','Y','Admin Executions Access Authority'),
    ('admin.execution:edit','Y','Admin Executions Edit Authority'),
    ('admin.batch','Y','Admin Batch Access Authority'),
    ('admin.batch:edit','Y','Admin Batch Edit Authority'),
    ('admin.board','Y','Admin Board Access Authority'),
    ('admin.board:edit','Y','Admin Boards Edit Authority'),
    ('admin.page','Y','Admin Page Access Authority'),
    ('admin.page:edit','Y','Admin Pages Edit Authority'),
    ('admin.git','Y','Admin Git Access Authority'),
    ('admin.git:edit','Y','Admin Git Edit Authority'),
    ('admin.template','Y','Admin Template Access Authority'),
    ('admin.template:edit','Y','Admin Template Edit Authority'),
    ('admin.notification','Y','Admin Notification Access Authority'),
    ('admin.notification:edit','Y','Admin Notification Edit Authority'),
    ('admin.verification','Y','Admin Verification Access Authority'),
    ('admin.verification:edit','Y','Admin Verification Edit Authority'),
    ('admin.storage','Y','Admin Storage Access Authority'),
    ('admin.storage:edit','Y','Admin Storage Edit Authority'),
    ('admin.discussion','Y','Admin Discussion Access Authority'),
    ('admin.discussion:edit','Y','Admin Discussion Edit Authority'),
    ('actuator','Y','Actuator Access Authority'),
    ('h2-console','Y','H2 Console Access Authority'),
    ('springdoc','Y','API Docs Docs Access Authority'),
    ('example', 'Y','Example Access Authority'),
    ('example:edit', 'Y', 'Example Edit Authority');

-- core_role
insert into `core_role`
    (`role_id`,`system_required`,`name`,`anonymous`, `authenticated`, `note`)
values
    ('DEFAULT','Y','Default Role','Y','Y','Default Role'),
    ('USER','Y','User Role','N','Y','User Role'),
    ('DEVELOPER','N','Developer Role','N','N','Developer Role');

-- core_role_authority
insert into `core_role_authority`
    (`role_id`,`authority_id`)
values
    ('DEFAULT','example'),
    ('DEFAULT','example:edit'),
    ('DEVELOPER','admin'),
    ('DEVELOPER','admin.common'),
    ('DEVELOPER','admin.monitor'),
    ('DEVELOPER','admin.user'),
    ('DEVELOPER','admin.security'),
    ('DEVELOPER','admin.menu'),
    ('DEVELOPER','admin.message'),
    ('DEVELOPER','admin.variable'),
    ('DEVELOPER','admin.code'),
    ('DEVELOPER','admin.board'),
    ('DEVELOPER','admin.page'),
    ('DEVELOPER','admin.git'),
    ('DEVELOPER','admin.template'),
    ('DEVELOPER','admin.notification'),
    ('DEVELOPER','admin.verification'),
    ('DEVELOPER','admin.storage'),
    ('DEVELOPER','admin.discussion'),
    ('DEVELOPER','admin.execution'),
    ('DEVELOPER', 'admin.batch'),
    ('DEVELOPER','actuator'),
    ('DEVELOPER','h2-console'),
    ('DEVELOPER','springdoc');

-- core_user
insert into `core_user`
    (`user_id`,`username`,`password`,`totp_secret`,`name`,`admin`,`status`,`email`,`mobile`, `join_at`, `password_at`)
values
    ('1fa8cd866e1a4baf94d62a6f3b71ac74','admin','{noop}admin','{noop}CSKN2SJE4PUJYRYAYJ4LSMV3LXO2HXLM','Administrator','Y','ACTIVE','admin@oopscraft.org','010-1234-5678', current_timestamp, current_timestamp),
    ('027d360d6a3a4a319959e29647815f9b','developer','{noop}developer','{noop}FW36O4RCP35ASLUR2C7RHSCRJ6HEXYDQ','Developer','N','ACTIVE', null, null, current_timestamp, current_timestamp),
    ('6edb6033f8ea40858179cd657e9b9c8e','user','{noop}user','{noop}J6WMGYTHQZ4TP3DB5PM4TXEEZUB37NYV','User','N','ACTIVE', null, null, current_timestamp, current_timestamp),
    ('27b91369bdee4e1ab77a2cecb70384ec','apple','{noop}apple','{noop}UVG5QBQDQHKX6X75CKMDM7AW32LTGHBB','Apple','N','ACTIVE', 'apple@oopscraft.org', null, current_timestamp, current_timestamp),
    ('5e676016aa644a6cb5f4e1fd4a469dce','orange','{noop}orange','{noop}VIJRCY6PVBN5DYORRBWUWIW6J67A2QTY','Orange','N','ACTIVE', 'orange@oopscraft.org', '010-1111-2222', current_timestamp, current_timestamp);

-- core_user_role
insert into `core_user_role`
    (`user_id`,`role_id`)
values
    ('027d360d6a3a4a319959e29647815f9b','DEVELOPER');

-- core_menu
insert into `core_menu`
    (`menu_id`,`enabled`,`parent_menu_id`,`link`,`target`,`sort`,`icon`)
values
    ('1e9fd6f84bfb4504ae0e067c45244907','Y',null,'/admin',null,1,'data:image/png;base64,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'),
    ('d1c6f5afbd9d40e4a4ad237b19cc21fb','Y',null, null,null,2, 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAAeP4ixAAAACXBIWXMAAAsTAAALEwEAmpwYAAADGUlEQVR4nO2aXWiOYRjHf5tvtldClOZzPpYcUCtf6aH5SCQ15WQ2ksY0lHKAmQMj+ZoDseRgQpQjDp2gfMxOKAlJZKRRvtYSenXX/9bd0/a2F+923+v91VXP9fQ89f7f+76u57qu54Esf8iVBc1c4IFsHoFyAfgFnJeZ44tAAYGRBG4CxfJnA/eBNmAfMIiAhCwESoEjwFggB1gDvALeAOt0znshkY7zgd3ADmAAkAfUAe3ALWAWHjJK5gqxTNTqrHb8Z8BXYCie0B/YCXyWdSTEsgg4DpwAlgItWi0veAp8ACplrcDiFNfnKegNe4AXQB88IAlcBjYDfYFhQC2wBejXyT01ip+RipdVeEBSW6kIOAYs0fkiZwvFmQBU6fgccFd/QI+SjMXECuAoMNnxjcApsfsOA1OB0wr8Vq1qj22zZAfBbRJAtWIg4fh7nSxlHpRN2l5uwnikpNDtJFNkqeGKhyr90535Wx2/HvgJXFOa9kKIZabiZUEKvz7mmzLnu84n8ESIZSVwUsHeVf8l8BbYlOm2IB0hqGisVumS30V/F/AFaM5kW5CuEMsY4CBQpgKyK36j2oIrwDhfhFiKFQdz0vDvqC2o/Z9twb8KQXu/AjgAjJZfHvMrVDlbfwPwTjFU6osQYmX/dpX9cT8R80foYWp+g1dCLIUq+20NNinmFyq7GdvosxBLiZ430x2/QdVA5Jj3QlBFXQnslzXERAQjxFIii0IXEqWwrBCX7IqkSZTdWmlsreX0ghVpUgFXk+G5bpRpId01140yLcQyRGV1u6bvZrgQpBBLgdMANWoeHKQQi3m18BD4ppUyZXeQQlADZGLmPfBcsRSkEIsZhx7SeOcGMINAhVjMiPQ68AM4ow4vSCEWU44/Bj4C29RzBCkEvXIwIj4BT4BlBCrEYtLzWaXrqynmvZFj69Wvj/dJiMW8GL2tB2qd3my5RLJpmjwO1DWnfBOCSpu1wGvNe8udcidyOtEcpfUWXWvu8ZLBes/Y5pQ7kSz+AYK51ntMuXNJ8XNPFuwnIYb5msQ36zhocnvDZ1N/zW/sPu+bsFdLvwAAAABJRU5ErkJggg=='),
    ('0231d214ffc6463fafe31c13797fdde1','Y','d1c6f5afbd9d40e4a4ad237b19cc21fb', '/example/hub-and-spoke',null,1,'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAAeP4ixAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAsElEQVR4nO2WQQoCMRAE+xUi/v9JoviaXhbm4FXpmWiogiHHUExBIsF/4cZ5SLruIGJJL0m3SZE04zJuFnm+na2ZdYtcJN0nNuNmEU3JeEBkRMZDIu0yHhQ5aZOZFmmTWSHSIrNKJC4z8bJ/8jfbQsSJC1djRAo2EsakVZBWGJNWQVphTFoFaYUxaRWkFcakVZBWGJNWQVphTFoFaYUxaRWkFcaktWta/pH5mm1EQMMcw5pTKIlq9sYAAAAASUVORK5CYII='),
    ('d97e96219e624d8591102495a2c799c3','Y','d1c6f5afbd9d40e4a4ad237b19cc21fb', '/example/master-detail',null,2,  'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAAeP4ixAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAsElEQVR4nO2WQQoCMRAE+xUi/v9JoviaXhbm4FXpmWiogiHHUExBIsF/4cZ5SLruIGJJL0m3SZE04zJuFnm+na2ZdYtcJN0nNuNmEU3JeEBkRMZDIu0yHhQ5aZOZFmmTWSHSIrNKJC4z8bJ/8jfbQsSJC1djRAo2EsakVZBWGJNWQVphTFoFaYUxaRWkFcakVZBWGJNWQVphTFoFaYUxaRWkFcaktWta/pH5mm1EQMMcw5pTKIlq9sYAAAAASUVORK5CYII='),
    ('51ea3c97b9384eb69b5d0f5530e00f4a','Y','d1c6f5afbd9d40e4a4ad237b19cc21fb', '/example/modal-overlay',null,3, 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAAeP4ixAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAsElEQVR4nO2WQQoCMRAE+xUi/v9JoviaXhbm4FXpmWiogiHHUExBIsF/4cZ5SLruIGJJL0m3SZE04zJuFnm+na2ZdYtcJN0nNuNmEU3JeEBkRMZDIu0yHhQ5aZOZFmmTWSHSIrNKJC4z8bJ/8jfbQsSJC1djRAo2EsakVZBWGJNWQVphTFoFaYUxaRWkFcakVZBWGJNWQVphTFoFaYUxaRWkFcaktWta/pH5mm1EQMMcw5pTKIlq9sYAAAAASUVORK5CYII='),
    ('fce30ba305ba4742a84cdc41996810fd','Y',null,'/page/board',null,3,'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAAAXNSR0IArs4c6QAABGJJREFUeF7tWl1oHUUUPmeuZSkaDdg+WIPcO7PJNQ0VQnxQsFANSAriiz/VUBRbKdVaX3yQCMWrICiCCLYGxIBgS6XxTUlVUiulIIIRqXg1zczubWgNFiWhtGCT3T0yyQaC3tzdzZ2Ylcy+hNw9880535yzM/NxENb5g+s8frAE2AwwzMDAmanNziwMElIfAN5oBp6uRYAnC4yeq9y/5Q8zmAsoxkugMjr1KSE8YtLJRSwEGK703va4SezUBBSLxdZCoXAPIrY0cqB/8JuPkRUcIrjIEL414WxEcC8itEVh+Nfx53c8lYB5JQiC72q12kyauVMRwDl/BgDeRcSbk0B3f3B23uTPC+PVw3t2dCXZp3n/wtCp6q3FrZ3a9ui++xKHENE1xtjLUsojScaJBAgh+gBgJG255IGAOGhCxJ1Syi8bkZBIQKlU+oEx1g0AIQC8iojn81wCRFQGgAoAFABgTCl1d1MEcM5nEXFDFEVf+L6/MymlDp2aGmYAjybZrex9dOK13tt3JY0tlUonGWN9RDTreZ7TFAFCCNIARDTseV7iF7hy+rdNYYSDSNSHiDclOZvmPRFdRaQRZHggzTbIOT+BiI9pbKVUwyxPLIGsBKQJaLVtLAE2A2wJ2G+A/QjaXcBug/YcYA9C9iRoj8L2LmAvQ/Y2aK/DVg/4TxWh1RY70uBbQcQKIlYQsYKIFUSsIGIFESuIWEHECiJWELGCSLOCSEEIEcQXkAki+jzNZWStbRDxIQBojz+CN8TNHXXdWvauzDnfhohHAeCutQ6oyfnPM8Z2TUxM/FgPpy4BnPNbEPEcANzR5OS5GE5El4Ig2DY5OTn9T4fqEuC67itE9EZsPEJE47mIJKMTiHgnAMy39RDRgOd5b6Yl4BgR9Wtjx3FaqtXq1Yxz58K8XC63BEFwRTuDiMeklLtTEZBFUclFpA2cSGrxWa4Ehohoj8YNw7CzVqv9mvdA6/nX3t7eGUVRNS6BIc/znk2bAf06ZWLjywBwloh0n2CWBxFxvlOUiH7Wf7IMXsha7CIiPU4HkWk8IhaIaDsibo7nfVIp9UkqAnSToRDiKwB4IKPTeTUfVUrpjtd/LeKy54Cenp4N09PTA4i493+8HV4AgA9bW1vfGhsbm0t9Dmh2GTnn+xFxUONEUfSe7/svrgRTCHEIAF6Py+h9z/MOrASn0ZjERsmsE+rMmZmZ0f3ERSK6zhhzpZQXs+Jo+7a2to2O4+j6L+r0JaJuz/N+WgnWcmOME2Bq9RcdFkI8AQDH4/+/Vkr15pYAk6u/NEghxBkA2K5/Q8SHpZSfmSLBaAaYXv3FIF3X7Sai7wGAEZFijHVJKa+bIMEYAau1+ktK4SMAeDrOgpeklO/kigDOeS8ijsZOHVZKHTTh4CJGuVzeMjc3Nx634P+ilNpqAt9YBnR0dGwKw/D0ws4XPej7/u8mHFyK4bruXiJ6GwCOKKX0Ftn0Y4yApj1ZIwBLwBoRn5tp130G/A33IBFuxy9ymgAAAABJRU5ErkJggg=='),
    ('01f9240a225f4b5c821e00a5fe1b9353','Y','fce30ba305ba4742a84cdc41996810fd','/board/anonymous',null,1,'data:image/png;base64,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'),
    ('188de70ba71e4d23bafa4a232379efff','Y','fce30ba305ba4742a84cdc41996810fd','/board/member',null,2,'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAAAXNSR0IArs4c6QAABW1JREFUWEe9l2lMVFcUx//nPsCFpglSl0TFXSsVq1XTGqsozow6g1RmQdMPSIxLXVps2qDWjRhs1VaIRuPauFcDzGCdBZkZlaq1torRUms0bY1pVVxQKIICwz3NG0ODsg7avk8v793zP79z7j3n3ksI8NFMMg0lwfGAeIvBXQGuZClukZAnJSPnuMt2IxBJaulgjd4SRcTrAdbW2gSHhDBLCZ/P59dh5hoS2CWFb9mxI0futES7RQAavSlBEPYw0DYyKgpjxo/DwDcGoX1oqN/HwwfFKLx4Cd7cXNy+eQsg3BTguDyH7UJzEM0CaPTm9wBpa9O2Hc2aP5eGDB/eqKaUEkftduRkZqnpKJEs3z7myrnWFESTADF6Yw+F6JeQkJDQlJUrqEevXs0F5P///XcnsXvbNvX11wdF4UMKCrZXN2bYJIBOb9zDRIlJs2dj1NjoFjmvHbR35w6cOp4PBuZ5ndYtAQOMj48Pp0pxp2v3bkrq2jUgana2nvFRWlKKJcnJXFVVfdXrsg4MGEATa04k5j3GaVMxKS4uoOhrB29Oz8DF8+chWQ5obC00GpZWb14L4pRFK1ei74D+rQLwuFzI3H8ADDJ5ndm2hkQaBaid/9UZ6ejUuXOrAH46cwY7Nm0GGAs8LuvmgAC0etMWED5YuWYNukV0bxXAyRMnsG/HThCQ5HZa9wQEoDGYlhKQNu/jhRg6YkSrAKwHD/n7Aog0Hkf2sYAAxsdaRgqWZ0ZFj0HSnDmtAljxaQqKbt180kZUhdvt9oqAAFJTU8WZc4XXleCg7p9npFNYh/CAIC4WFGDz+nR1h8jyOG0JAZehaqDVG5NAtKt3v75Qq0EI0SKI0tISrFqylEsfPmRFocF5duvl1gCQzmCZJSE3EtDmzWHDMDc5GUqQ0iREWdkjfLFiOe7duevfDyAww+Ow5QQEYLFYlJIK3zeASGDmciGojBld+vTri6nTE9Grd596esyMC+fOIXPffn5QXEwM/E5ADwBBYFrncWUvavEa0BlMaQwsleCCGlZiEYpHwRW+r1UgVSSiZ08MjBqEsLAOkLIGd4vuoPDSRS6+d1/t2D4pOc3rsq2aMNkUKSWcUEEIcz0O69bnIeo1orF6S5cg1NwAUbEvuCYy//DhklqjCQbjOClpEQRiAAQ/I0b4G8C3Usq0um1X3VEFQV0DlaI6NMLt3lde164egFZvngPircRIcbusXzaUNt1k0+sscaXOP1+QbBuem3tAhaj3aA3mrwD+hATi3Xbr4SYBdJPMGSx4IRON8zqy8xsBmMISOf2Hj0bl43LcuHwBxHjH7bL+2AiABeDMhoKqlwGdwbSdgVkMGel15tSN8l9trcG4GqDPRpuS8KS8DOeOWsGED70O66aGADSxlneJ5SmA13qctsVNZ6AFABp9fB6R0MXNW4YnFWVw794AYt7rdtmm/x8ApDGY7r/yaliYfnYKgRm2jansq6pq9ODxUjMQM2lKH0Uov3UbEIWRk9/3B3zi0Hbc/+s6VwfXdKhbNbXZeKkAOoN5GoMPDo6eiAEjnp4TL+W7cO38KQhwTJ7TduL5aXipALUlNTZhJjpGPO2If179GWftBwHQYo8ze+1/C6A35pMQ0XELliOkTTu/r/LSh3DtWAciWN0Oq/mFANRbEAgflbcX2h+ysh7XEzMY7ypKcMfohJl4rava6oGiP67hdM5u9Wp2xeO0Rj5vo56wRaXIZcJyr9Oa12QZNlRGdb9p9ab5DN6gKIoYEhNL1ZWVKDztZjBXE9EMtyP7QHMaLwSgGuv0xjEQIouZOz0Vo9sspdGbazsbiHO/ZaAGteMnTpzS06co+wlUUc2UmO/KKmqN1j+PxZs/bL7TnQAAAABJRU5ErkJggg=='),
    ('408f6d1824e143d18d3e4ef24ffedabc','Y','fce30ba305ba4742a84cdc41996810fd','/board/notice',null,3,'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAAAXNSR0IArs4c6QAAAmBJREFUWEft1kvIzmkYBvDfJxlKSmMWRIoiOTSRU6EsJHIuh4zlsDEk7FiyYzYTK7KymBHlkNNCiaKhJElT0gjNwoJkYyi66vnr/d7e03eoT/nuehfv89yH67me+77+T5cBtq4Brq8vAMZjYTnAHbzszWH6AmAj/ipFN+HMIID+ZGADLuH/FkkXYG/Z/x13W/gOwyqcq/dp1AP7cARXsB4fenOympgUT6+sxUEcrs1XD2A0HmNscbqANFvFxCiswxpMx4Ti96LEnUd+78p6iqc54x/7r8S9qUA0YmAqbmAc3mNRSf4bDmBMG0Ze4xCOYQZuY2QpvhT/tGKg2guIi/gVj/AnltUEfsYTPC9rEzGNbrpyDVswEyexur54YlvpwFAMxy38XAqFkfTHCbyqYyLCtL00Zk4ce4DFpY8+NWKunRCdRSYiFiZyl2+xu/TClEJvbe5JpQ9Cfyw9EKFqaK0ArMDlEvUU8zGrdPRPNdka5fgRfyNgYstxvacM3Mcc5L7nYghuYkRJFCYy+wHayAI434gAvId5PQEwGTl1LNewGQ/LCGXtFPbUjFszhiM80ZJYcj6rd2x2BRm5P4pzdCDNF2GKXcXKwkyzwtV67j4TFNuJ450COFojsxmxXdhfgmeX7m5XPPuJ/bc4JmeV42tsMwZOY2vx+gE7sKT8/wUfO6mOKGEl5cm5rVMGot2hPtZuVNthSRPHGo5js+SDAAYZ+L4ZyPjlrZc3X6zpl6zd/JX96ume70bejt2e743GsJrbDvP32K1bzW8SQKWAPT5ahwFtr6DDPP3j1led7zOKL04qfSGcgtpYAAAAAElFTkSuQmCC'),
    ('bb9cdea4a96b46958468f3aca15832bc','Y',null,null,null,4, 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAAAXNSR0IArs4c6QAAAplJREFUeF7t2svrDlEcx/HXD7kkt0QiEomEiLAgdpSibGysSPZSbotfWViw4A9Qdna2LGVjgYVLIpHcErknkmunnqlf0/ye58zz/Hp+M86c3TTfM+d83vM533POnBmQeBlIXL8GQOOAxAk0Q6CkAf6WjO8l/C2u4xyu9fKgdnXLOqCfALJ+hzZP4xhGvP06AMhADOLkSDuhTgDC29+LiyMJoVcAZevH9n0O5rcEH8SEVsXv2IobsQ/qFFdWQH4Mlq3fqT9F9/fj/JAbr7EBL7p5WL5OWQGjAWAaPuU6fhub8bVXCHUAsA43C4Rexk787gVCHQCcweFhRIbp8cj/DCAkw0eY0kbkPlzoFkLVHXAJuzuI+4ltuNoNhKoD2NSy+A7abt3fYyMel4VQdQCZnpAIT2BXGxAPsL7szFAXABmIFTiOPRhT8LbDeuFAGRfUDUCmbQ3OYktO7B8ESMENUaWuAIK40PejOJVTGsAcilLfIbEUPWM0VoKdtATbh+VyVu5gdadK2f06OyDTsB1Xhgj+jOkpAViLWznB0S82OrDVQBWHwCoE2w8t0bqiAysMYCXupgwgTHv3UgawHPdTBrCsYOETPbSjAyucA5biYcoOWNL6ZpDsLLC4YBsc7ezowAoPgUV4kvIQWIinKQNYgGcpAwgnSM9TBjAPL1MGMBevUgYQzg7CeWGy64DZeJMygFkIv9Mk64CZeJcygBn4kDKA8AH0Y8oApiJ8CU42B4Sj8y8pA5hccCAavcuNDqzwdngSvqXsgIkIv88lmwPG40fKAMYh/CaTrAPG4lfKAEIiDz9GjIoDcu1W5jJ6dosOHGYarIzixgHNEOhPDqiq5bvuV9kc0HVDVa3YAKjqm+lXvxoH9It0Vdv5BwSbf0GhLrNkAAAAAElFTkSuQmCC'),
    ('57c2454133be4fe9b7bf352053186187','Y','bb9cdea4a96b46958468f3aca15832bc','/git/arch4j/docs/01.architecture-overview/index.md',null,1,'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAAAXNSR0IArs4c6QAAAplJREFUeF7t2svrDlEcx/HXD7kkt0QiEomEiLAgdpSibGysSPZSbotfWViw4A9Qdna2LGVjgYVLIpHcErknkmunnqlf0/ye58zz/Hp+M86c3TTfM+d83vM533POnBmQeBlIXL8GQOOAxAk0Q6CkAf6WjO8l/C2u4xyu9fKgdnXLOqCfALJ+hzZP4xhGvP06AMhADOLkSDuhTgDC29+LiyMJoVcAZevH9n0O5rcEH8SEVsXv2IobsQ/qFFdWQH4Mlq3fqT9F9/fj/JAbr7EBL7p5WL5OWQGjAWAaPuU6fhub8bVXCHUAsA43C4Rexk787gVCHQCcweFhRIbp8cj/DCAkw0eY0kbkPlzoFkLVHXAJuzuI+4ltuNoNhKoD2NSy+A7abt3fYyMel4VQdQCZnpAIT2BXGxAPsL7szFAXABmIFTiOPRhT8LbDeuFAGRfUDUCmbQ3OYktO7B8ESMENUaWuAIK40PejOJVTGsAcilLfIbEUPWM0VoKdtATbh+VyVu5gdadK2f06OyDTsB1Xhgj+jOkpAViLWznB0S82OrDVQBWHwCoE2w8t0bqiAysMYCXupgwgTHv3UgawHPdTBrCsYOETPbSjAyucA5biYcoOWNL6ZpDsLLC4YBsc7ezowAoPgUV4kvIQWIinKQNYgGcpAwgnSM9TBjAPL1MGMBevUgYQzg7CeWGy64DZeJMygFkIv9Mk64CZeJcygBn4kDKA8AH0Y8oApiJ8CU42B4Sj8y8pA5hccCAavcuNDqzwdngSvqXsgIkIv88lmwPG40fKAMYh/CaTrAPG4lfKAEIiDz9GjIoDcu1W5jJ6dosOHGYarIzixgHNEOhPDqiq5bvuV9kc0HVDVa3YAKjqm+lXvxoH9It0Vdv5BwSbf0GhLrNkAAAAAElFTkSuQmCC'),
    ('92bdfc7c38d6438680ff41420eb4ebb6','Y','bb9cdea4a96b46958468f3aca15832bc','/git/arch4j/docs/02.development-guide/index.md',null,2,'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAAAXNSR0IArs4c6QAAAplJREFUeF7t2svrDlEcx/HXD7kkt0QiEomEiLAgdpSibGysSPZSbotfWViw4A9Qdna2LGVjgYVLIpHcErknkmunnqlf0/ye58zz/Hp+M86c3TTfM+d83vM533POnBmQeBlIXL8GQOOAxAk0Q6CkAf6WjO8l/C2u4xyu9fKgdnXLOqCfALJ+hzZP4xhGvP06AMhADOLkSDuhTgDC29+LiyMJoVcAZevH9n0O5rcEH8SEVsXv2IobsQ/qFFdWQH4Mlq3fqT9F9/fj/JAbr7EBL7p5WL5OWQGjAWAaPuU6fhub8bVXCHUAsA43C4Rexk787gVCHQCcweFhRIbp8cj/DCAkw0eY0kbkPlzoFkLVHXAJuzuI+4ltuNoNhKoD2NSy+A7abt3fYyMel4VQdQCZnpAIT2BXGxAPsL7szFAXABmIFTiOPRhT8LbDeuFAGRfUDUCmbQ3OYktO7B8ESMENUaWuAIK40PejOJVTGsAcilLfIbEUPWM0VoKdtATbh+VyVu5gdadK2f06OyDTsB1Xhgj+jOkpAViLWznB0S82OrDVQBWHwCoE2w8t0bqiAysMYCXupgwgTHv3UgawHPdTBrCsYOETPbSjAyucA5biYcoOWNL6ZpDsLLC4YBsc7ezowAoPgUV4kvIQWIinKQNYgGcpAwgnSM9TBjAPL1MGMBevUgYQzg7CeWGy64DZeJMygFkIv9Mk64CZeJcygBn4kDKA8AH0Y8oApiJ8CU42B4Sj8y8pA5hccCAavcuNDqzwdngSvqXsgIkIv88lmwPG40fKAMYh/CaTrAPG4lfKAEIiDz9GjIoDcu1W5jJ6dosOHGYarIzixgHNEOhPDqiq5bvuV9kc0HVDVa3YAKjqm+lXvxoH9It0Vdv5BwSbf0GhLrNkAAAAAElFTkSuQmCC'),
    ('2b3dffc2804a43edb8c600841feb5617','Y','bb9cdea4a96b46958468f3aca15832bc','/git/arch4j/docs/03.operation-manual/index.md',null,2,'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAAAXNSR0IArs4c6QAAAplJREFUeF7t2svrDlEcx/HXD7kkt0QiEomEiLAgdpSibGysSPZSbotfWViw4A9Qdna2LGVjgYVLIpHcErknkmunnqlf0/ye58zz/Hp+M86c3TTfM+d83vM533POnBmQeBlIXL8GQOOAxAk0Q6CkAf6WjO8l/C2u4xyu9fKgdnXLOqCfALJ+hzZP4xhGvP06AMhADOLkSDuhTgDC29+LiyMJoVcAZevH9n0O5rcEH8SEVsXv2IobsQ/qFFdWQH4Mlq3fqT9F9/fj/JAbr7EBL7p5WL5OWQGjAWAaPuU6fhub8bVXCHUAsA43C4Rexk787gVCHQCcweFhRIbp8cj/DCAkw0eY0kbkPlzoFkLVHXAJuzuI+4ltuNoNhKoD2NSy+A7abt3fYyMel4VQdQCZnpAIT2BXGxAPsL7szFAXABmIFTiOPRhT8LbDeuFAGRfUDUCmbQ3OYktO7B8ESMENUaWuAIK40PejOJVTGsAcilLfIbEUPWM0VoKdtATbh+VyVu5gdadK2f06OyDTsB1Xhgj+jOkpAViLWznB0S82OrDVQBWHwCoE2w8t0bqiAysMYCXupgwgTHv3UgawHPdTBrCsYOETPbSjAyucA5biYcoOWNL6ZpDsLLC4YBsc7ezowAoPgUV4kvIQWIinKQNYgGcpAwgnSM9TBjAPL1MGMBevUgYQzg7CeWGy64DZeJMygFkIv9Mk64CZeJcygBn4kDKA8AH0Y8oApiJ8CU42B4Sj8y8pA5hccCAavcuNDqzwdngSvqXsgIkIv88lmwPG40fKAMYh/CaTrAPG4lfKAEIiDz9GjIoDcu1W5jJ6dosOHGYarIzixgHNEOhPDqiq5bvuV9kc0HVDVa3YAKjqm+lXvxoH9It0Vdv5BwSbf0GhLrNkAAAAAElFTkSuQmCC'),
    ('767c59aa3b2344a7bbfdabead7b6c4a3','Y',null,'https://github.com/chomookun/arch4j','_blank',4,'data:image/png;base64,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');

-- core_menu_i18n
insert into `core_menu_i18n`
    (`menu_id`,`locale`,`name`)
values
    ('1e9fd6f84bfb4504ae0e067c45244907','en','Admin Console'),
    ('d1c6f5afbd9d40e4a4ad237b19cc21fb','en','Example'),
    ('0231d214ffc6463fafe31c13797fdde1','en','Hub and Spoke'),
    ('d97e96219e624d8591102495a2c799c3','en','Master Detail'),
    ('51ea3c97b9384eb69b5d0f5530e00f4a','en','Modal Overlay'),
    ('fce30ba305ba4742a84cdc41996810fd','en','Board Demo'),
    ('01f9240a225f4b5c821e00a5fe1b9353','en','Anonymous Board'),
    ('188de70ba71e4d23bafa4a232379efff','en','Member Board'),
    ('408f6d1824e143d18d3e4ef24ffedabc','en','Notice Board'),
    ('bb9cdea4a96b46958468f3aca15832bc','en','Documentation'),
    ('57c2454133be4fe9b7bf352053186187','en','Architecture Overview'),
    ('92bdfc7c38d6438680ff41420eb4ebb6','en','Development Guide'),
    ('2b3dffc2804a43edb8c600841feb5617','en','Operation Manual'),
    ('767c59aa3b2344a7bbfdabead7b6c4a3','en','Git Repository'),
    ('1e9fd6f84bfb4504ae0e067c45244907', 'ko', '관리자 콘솔'),
    ('d1c6f5afbd9d40e4a4ad237b19cc21fb','ko','예제'),
    ('0231d214ffc6463fafe31c13797fdde1','ko','허브 앤 스포크'),
    ('d97e96219e624d8591102495a2c799c3','ko','마스터 디테일'),
    ('51ea3c97b9384eb69b5d0f5530e00f4a','ko','모달 오버레이'),
    ('fce30ba305ba4742a84cdc41996810fd', 'ko', '게시판 데모'),
    ('01f9240a225f4b5c821e00a5fe1b9353', 'ko', '익명 게시판'),
    ('188de70ba71e4d23bafa4a232379efff', 'ko', '회원 게시판'),
    ('408f6d1824e143d18d3e4ef24ffedabc', 'ko', '공지 게시판');

-- core_message
insert into `core_message`
    (`message_id`,`name`)
values
    ('core.example.Example','Example'),
    ('core.example.Example.exampleId','Example ID'),
    ('core.example.Example.name','Example name'),
    ('core.example.Example.icon','Example icon'),
    ('core.example.Example.number','Example number'),
    ('core.example.Example.decimalNumber','Example bigDecimalNumber'),
    ('core.example.Example.dateTime','Example dateTime'),
    ('core.example.Example.instantDateTime','Example instantDateTime'),
    ('core.example.Example.zonedDateTime','Example zonedDateTime'),
    ('core.example.Example.date','Example date'),
    ('core.example.Example.time','Example time'),
    ('core.example.Example.enabled','Example enabled'),
    ('core.example.Example.type','Example type'),
    ('core.example.Example.code','Example code'),
    ('core.example.Example.text','Example text'),
    ('core.example.Example.cryptoText','Example cryptoText'),
    ('core.example.ExampleItem','Example item'),
    ('core.example.ExampleItem.itemId','Example itemId'),
    ('core.example.ExampleItem.name','ExampleItem name'),
    ('core.example.ExampleItem.percentage','ExampleItem percentage'),
    ('core.example.ExampleItem.dateTime','ExampleItem dateTime'),
    ('core.example.ExampleItem.enabled','ExampleItem enabled');

-- core_message_i18n
insert into `core_message_i18n`
    (`message_id`,`locale`,`value`)
values
    ('core.example.Example','en','Example'),
    ('core.example.Example.exampleId','en','Example ID'),
    ('core.example.Example.name','en','Name'),
    ('core.example.Example.icon','en','Icon'),
    ('core.example.Example.number','en','Number'),
    ('core.example.Example.decimalNumber','en','Decimal'),
    ('core.example.Example.dateTime','en','DateTime'),
    ('core.example.Example.instantDateTime','en','DateTime(UTC)'),
    ('core.example.Example.zonedDateTime','en','DateTime(Zoned)'),
    ('core.example.Example.date','en','Date'),
    ('core.example.Example.time','en','Time'),
    ('core.example.Example.enabled','en','Enabled'),
    ('core.example.Example.type','en','Type'),
    ('core.example.Example.code','en','Code'),
    ('core.example.Example.text','en','Text'),
    ('core.example.Example.cryptoText','en','Text(Crypto)'),
    ('core.example.ExampleItem','en','Item'),
    ('core.example.ExampleItem.itemId','en','Item ID'),
    ('core.example.ExampleItem.name','en','Item Name'),
    ('core.example.ExampleItem.percentage','en','Percentage'),
    ('core.example.ExampleItem.dateTime','en','DateTime'),
    ('core.example.ExampleItem.enabled','en','Enabled'),
    ('core.example.Example','ko','예제'),
    ('core.example.Example.exampleId','ko','아이디'),
    ('core.example.Example.name','ko','이름'),
    ('core.example.Example.icon','ko','아이콘'),
    ('core.example.Example.number','ko','정수'),
    ('core.example.Example.decimalNumber','ko','실수'),
    ('core.example.Example.dateTime','ko','일시'),
    ('core.example.Example.instantDateTime','ko','일시(UTC)'),
    ('core.example.Example.zonedDateTime','ko','일시(시간대)'),
    ('core.example.Example.date','ko','일자'),
    ('core.example.Example.time','ko','시간'),
    ('core.example.Example.enabled','ko','사용여부'),
    ('core.example.Example.type','ko','타입'),
    ('core.example.Example.code','ko','코드'),
    ('core.example.Example.text','ko','문자열'),
    ('core.example.Example.cryptoText','ko','문자열(암호화)'),
    ('core.example.ExampleItem','ko','아이템'),
    ('core.example.ExampleItem.itemId','ko','아이템ID'),
    ('core.example.ExampleItem.name','ko','아이템명');

-- variable
insert into `core_variable`
    (`variable_id`,`name`,`value`)
values
    ('core.test','Test Variable', 'test_value');

-- core_code
insert into `core_code`
    (`code_id`)
values
    ('core.example.Example.code');

-- core_code_i18n
insert into `core_code_i18n`
    (`code_id`, `locale`, `name`)
values
    ('core.example.Example.code', 'en', 'Code'),
    ('core.example.Example.code', 'ko', '예제코드');

-- core_code_item
insert into `core_code_item`
    (`code_id`,`item_id`,`sort`, `enabled`)
values
    ('core.example.Example.code','STUDENT',1, 'Y'),
    ('core.example.Example.code','TEACHER',2, 'Y');

-- core_code_item_i18n
insert into `core_code_item_i18n`
    (`code_id`,`item_id`,`locale`,`name`)
values
    ('core.example.Example.code','STUDENT','en','Student'),
    ('core.example.Example.code','TEACHER','en','Teacher'),
    ('core.example.Example.code','STUDENT','ko','학생'),
    ('core.example.Example.code','TEACHER','ko','교사');

-- core_storage
insert into `core_storage`
    (`storage_id`,`system_required`,`name`,`client_type`,`client_properties`)
values
    ('web', null,'Web Storage','LOCAL','location=${user.home}/.arch4j/board'),
    ('batch', null,'Batch Storage','LOCAL','location=${user.home}/.arch4j/batch'),
    ('test', null,'Test Storage','LOCAL', 'location=${user.home}/.arch4j/test');

-- core_discussion
insert into `core_discussion`
    (`discussion_id`,`name`,`provider_type`,`provider_properties`)
values
    ('e355926fdadc41f49bf93afe76af1496','Default','default',''),
    ('7eddf209186d487ab58e30a090944d79','Gisqus','gisqus',
'repo=chomookun/arch4j
repo-id=R_kgDOJKtMwQ
category=Giscus
category-id=DIC_kwDOJKtMwc4Cpe0A
');

-- core_board
insert into `core_board` (
    `board_id`,
    `board_name`,
    `icon`,
    `message_format`,
    `message`,
    `skin`,
    `page_size`,
    `file_enabled`,
    `storage_id`,
    `discussion_enabled`,
    `discussion_id`
) values (
     'anonymous',
     'Anonymous Board',
     'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAAAXNSR0IArs4c6QAAAuRJREFUWEftlttL1FEQx+ec32V31dVdt5WtvFFpIoG6iq0LCZH4FIT01F9QtHYh6CUIliIIIggz6ql8KILwpctbl4colbQto4u3yFTM9bLuthfX8/udM/GLCtYsLwhG7Hk7nDMzn/nOwAyBdT5kneNDGuDfUcDaOuZQCdzWUNSrEg4zTrqEID0ceWCOmXvhpCu+on65MJFpUZMVEpHchGKNLKFH56RYIvQpRzgQbc6fMfz9UiDryujlIqt08JzXpnyK6BCYnOfdQaaNRXUTAKAq02HGsQsRegiSgArJV6GjJV8NJ7ktg9kMzFVI0E0I1CiUeDQuig3/Tos0vz1XVkptqrQxU4Lr72LsS0JcizUXHEsBcF793HGkKqfu9M6clERjDKF3msHrKQaBIBPdQcaGI5oJAUCl9ImGQBDFbsPIlSHPl+XKaqldodtsMmzNlsGipFb55oc4tA/GO0O+Iu8CgJHO4+5sz6naVIDFZE/oCG+mGDTdn9a5QDhblyNvsclglpZuqVt9cbgzkOia9RXWrRrgJ9Se9kmIMgEX623Lbo81BTj0OATBOIcTbmsaIK1AWoG0AmkF/gMF7K0jd/eVWPbeaNxAl5vOambB+e6IeD6h3YscLmhKmYbWlpFdhMIjh5kKd54qVbtMSqVTgUqnCsYisdjZ/2AKQkkBZzyLj/CZpIChsAZDEQ6DIU3rD+s8ygRBgg1RX9GzFADjYr40Xkip3gBEVGcoxMt0Us4RVLuJMneeQmpcJqXCqYIB5jBLUN42znVE0tbooBEmvgf6GNagP6RpA2EdYxqqlABTKL5PcuwApC9Rkh7O+TaP/kzo7xuEH+Us+2iZsWoBUrdFBa8mxA5dEItEECnQWW54Qm4XQAmlOCcReKtptAOICBirW2y2oA/8RP9TWZdeYRZa+pFabeMlQtbzLfGMF8bzXGailuryWDS8aRD8RCy3h34rwUoM1+rvyhVYq8g//KQB1l2Bb6xnBD8tREAmAAAAAElFTkSuQmCC',
     'MARKDOWN',
     '**Anonymous Board Demo**
* Accessible for non-logged-in users
* Read/Write enabled
',
     '_default',
     10,
     'Y',
     'web',
     'Y',
     '7eddf209186d487ab58e30a090944d79'
 ), (
     'member',
     'Member Board',
     'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAAAXNSR0IArs4c6QAABW1JREFUWEe9l2lMVFcUx//nPsCFpglSl0TFXSsVq1XTGqsozow6g1RmQdMPSIxLXVps2qDWjRhs1VaIRuPauFcDzGCdBZkZlaq1torRUms0bY1pVVxQKIICwz3NG0ODsg7avk8v793zP79z7j3n3ksI8NFMMg0lwfGAeIvBXQGuZClukZAnJSPnuMt2IxBJaulgjd4SRcTrAdbW2gSHhDBLCZ/P59dh5hoS2CWFb9mxI0futES7RQAavSlBEPYw0DYyKgpjxo/DwDcGoX1oqN/HwwfFKLx4Cd7cXNy+eQsg3BTguDyH7UJzEM0CaPTm9wBpa9O2Hc2aP5eGDB/eqKaUEkftduRkZqnpKJEs3z7myrnWFESTADF6Yw+F6JeQkJDQlJUrqEevXs0F5P///XcnsXvbNvX11wdF4UMKCrZXN2bYJIBOb9zDRIlJs2dj1NjoFjmvHbR35w6cOp4PBuZ5ndYtAQOMj48Pp0pxp2v3bkrq2jUgana2nvFRWlKKJcnJXFVVfdXrsg4MGEATa04k5j3GaVMxKS4uoOhrB29Oz8DF8+chWQ5obC00GpZWb14L4pRFK1ei74D+rQLwuFzI3H8ADDJ5ndm2hkQaBaid/9UZ6ejUuXOrAH46cwY7Nm0GGAs8LuvmgAC0etMWED5YuWYNukV0bxXAyRMnsG/HThCQ5HZa9wQEoDGYlhKQNu/jhRg6YkSrAKwHD/n7Aog0Hkf2sYAAxsdaRgqWZ0ZFj0HSnDmtAljxaQqKbt180kZUhdvt9oqAAFJTU8WZc4XXleCg7p9npFNYh/CAIC4WFGDz+nR1h8jyOG0JAZehaqDVG5NAtKt3v75Qq0EI0SKI0tISrFqylEsfPmRFocF5duvl1gCQzmCZJSE3EtDmzWHDMDc5GUqQ0iREWdkjfLFiOe7duevfDyAww+Ow5QQEYLFYlJIK3zeASGDmciGojBld+vTri6nTE9Grd596esyMC+fOIXPffn5QXEwM/E5ADwBBYFrncWUvavEa0BlMaQwsleCCGlZiEYpHwRW+r1UgVSSiZ08MjBqEsLAOkLIGd4vuoPDSRS6+d1/t2D4pOc3rsq2aMNkUKSWcUEEIcz0O69bnIeo1orF6S5cg1NwAUbEvuCYy//DhklqjCQbjOClpEQRiAAQ/I0b4G8C3Usq0um1X3VEFQV0DlaI6NMLt3lde164egFZvngPircRIcbusXzaUNt1k0+sscaXOP1+QbBuem3tAhaj3aA3mrwD+hATi3Xbr4SYBdJPMGSx4IRON8zqy8xsBmMISOf2Hj0bl43LcuHwBxHjH7bL+2AiABeDMhoKqlwGdwbSdgVkMGel15tSN8l9trcG4GqDPRpuS8KS8DOeOWsGED70O66aGADSxlneJ5SmA13qctsVNZ6AFABp9fB6R0MXNW4YnFWVw794AYt7rdtmm/x8ApDGY7r/yaliYfnYKgRm2jansq6pq9ODxUjMQM2lKH0Uov3UbEIWRk9/3B3zi0Hbc/+s6VwfXdKhbNbXZeKkAOoN5GoMPDo6eiAEjnp4TL+W7cO38KQhwTJ7TduL5aXipALUlNTZhJjpGPO2If179GWftBwHQYo8ze+1/C6A35pMQ0XELliOkTTu/r/LSh3DtWAciWN0Oq/mFANRbEAgflbcX2h+ysh7XEzMY7ypKcMfohJl4rava6oGiP67hdM5u9Wp2xeO0Rj5vo56wRaXIZcJyr9Oa12QZNlRGdb9p9ab5DN6gKIoYEhNL1ZWVKDztZjBXE9EMtyP7QHMaLwSgGuv0xjEQIouZOz0Vo9sspdGbazsbiHO/ZaAGteMnTpzS06co+wlUUc2UmO/KKmqN1j+PxZs/bL7TnQAAAABJRU5ErkJggg==',
     'TEXT',
     'Member Board',
     '_default',
     10,
     'Y',
     'web',
     'Y',
     'e355926fdadc41f49bf93afe76af1496'
), (
     'notice',
     'Notice Board',
     'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAAAXNSR0IArs4c6QAAAmBJREFUWEft1kvIzmkYBvDfJxlKSmMWRIoiOTSRU6EsJHIuh4zlsDEk7FiyYzYTK7KymBHlkNNCiaKhJElT0gjNwoJkYyi66vnr/d7e03eoT/nuehfv89yH67me+77+T5cBtq4Brq8vAMZjYTnAHbzszWH6AmAj/ipFN+HMIID+ZGADLuH/FkkXYG/Z/x13W/gOwyqcq/dp1AP7cARXsB4fenOympgUT6+sxUEcrs1XD2A0HmNscbqANFvFxCiswxpMx4Ti96LEnUd+78p6iqc54x/7r8S9qUA0YmAqbmAc3mNRSf4bDmBMG0Ze4xCOYQZuY2QpvhT/tGKg2guIi/gVj/AnltUEfsYTPC9rEzGNbrpyDVswEyexur54YlvpwFAMxy38XAqFkfTHCbyqYyLCtL00Zk4ce4DFpY8+NWKunRCdRSYiFiZyl2+xu/TClEJvbe5JpQ9Cfyw9EKFqaK0ArMDlEvUU8zGrdPRPNdka5fgRfyNgYstxvacM3Mcc5L7nYghuYkRJFCYy+wHayAI434gAvId5PQEwGTl1LNewGQ/LCGXtFPbUjFszhiM80ZJYcj6rd2x2BRm5P4pzdCDNF2GKXcXKwkyzwtV67j4TFNuJ450COFojsxmxXdhfgmeX7m5XPPuJ/bc4JmeV42tsMwZOY2vx+gE7sKT8/wUfO6mOKGEl5cm5rVMGot2hPtZuVNthSRPHGo5js+SDAAYZ+L4ZyPjlrZc3X6zpl6zd/JX96ume70bejt2e743GsJrbDvP32K1bzW8SQKWAPT5ahwFtr6DDPP3j1led7zOKL04qfSGcgtpYAAAAAElFTkSuQmCC',
     'TEXT',
     'Notice Board',
     '_default',
     10,
     'Y',
     'web',
     null,
    null
);

-- core_board_i18n
insert into `core_board_i18n` (
    `board_id`,
    `locale`,
    `board_name`,
    `message`
) values (
     'anonymous',
     'ko',
     '익명 게시판',
     '**익명게시판 데모**
* 로그인하지 않은 사용자 접근가능
* 읽기/쓰기 가능
'
);

-- core_article
insert into core_article (
    article_id,
    board_id,
    created_at,
    user_id,
    title,
    format,
    content
) values (
    '8fb7622000744407af0d762c71fe02f1',
    'anonymous',
    '2023-07-15 14:01:17.777',
    '6edb6033f8ea40858179cd657e9b9c8e',
    'Markdown-it demo page contents (copy)',
    'MARKDOWN',
    'You will like those projects!
---
__Advertisement :)__

- __[pica](https://nodeca.github.io/pica/demo/)__ - high quality and fast image
resize in browser.
- __[babelfish](https://github.com/nodeca/babelfish/)__ - developer friendly
i18n with plurals support and easy syntax.

---

# h1 Heading 8-)
## h2 Heading
### h3 Heading
#### h4 Heading
##### h5 Heading
###### h6 Heading


## Horizontal Rules

___

---

***


## Typographic replacements

Enable typographer option to see result.

(c) (C) (r) (R) (tm) (TM) (p) (P) +-

test.. test... test..... test?..... test!....

!!!!!! ???? ,,  -- ---

"Smartypants, double quotes" and ''single quotes''


## Emphasis

**This is bold text**

__This is bold text__

*This is italic text*

_This is italic text_

~~Strikethrough~~


## Blockquotes


> Blockquotes can also be nested...
>> ...by using additional greater-than signs right next to each other...
> > > ...or with spaces between arrows.


## Lists

Unordered

+ Create a list by starting a line with `+`, `-`, or `*`
+ Sub-lists are made by indenting 2 spaces:
- Marker character change forces new list start:
 * Ac tristique libero volutpat at
 + Facilisis in pretium nisl aliquet
 - Nulla volutpat aliquam velit
+ Very easy!

Ordered

1. Lorem ipsum dolor sit amet
2. Consectetur adipiscing elit
3. Integer molestie lorem at massa


1. You can use sequential numbers...
1. ...or keep all the numbers as `1.`

Start numbering with offset:

57. foo
1. bar


## Code

Inline `code`

Indented code

 // Some comments
 line 1 of code
 line 2 of code
 line 3 of code


Block code "fences"

```
Sample text here...
```

Syntax highlighting

``` js
var foo = function (bar) {
return bar++;
};

console.log(foo(5));
```

## Tables

| Option | Description |
| ------ | ----------- |
| data   | path to data files to supply the data that will be passed into templates. |
| engine | engine to be used for processing templates. Handlebars is the default. |
| ext    | extension to be used for dest files. |

Right aligned columns

| Option | Description |
| ------:| -----------:|
| data   | path to data files to supply the data that will be passed into templates. |
| engine | engine to be used for processing templates. Handlebars is the default. |
| ext    | extension to be used for dest files. |


## Links

[link text](http://dev.nodeca.com)

[link with title](http://nodeca.github.io/pica/demo/ "title text!")

Autoconverted link https://github.com/nodeca/pica (enable linkify to see)


## Images

![Minion](https://octodex.github.com/images/minion.png)
![Stormtroopocat](https://octodex.github.com/images/stormtroopocat.jpg "The Stormtroopocat")

Like links, Images also have a footnote style syntax

![Alt text][id]

With a reference later in the document defining the URL location:

[id]: https://octodex.github.com/images/dojocat.jpg  "The Dojocat"


## Plugins

The killer feature of `markdown-it` is very effective support of
[syntax plugins](https://www.npmjs.org/browse/keyword/markdown-it-plugin).


### [Emojies](https://github.com/markdown-it/markdown-it-emoji)

> Classic markup: :wink: :crush: :cry: :tear: :laughing: :yum:
>
> Shortcuts (emoticons): :-) :-( 8-) ;)

see [how to change output](https://github.com/markdown-it/markdown-it-emoji#change-output) with twemoji.


### [Subscript](https://github.com/markdown-it/markdown-it-sub) / [Superscript](https://github.com/markdown-it/markdown-it-sup)

- 19^th^
- H~2~O


### [\\<ins>](https://github.com/markdown-it/markdown-it-ins)

++Inserted text++


### [\\<mark>](https://github.com/markdown-it/markdown-it-mark)

==Marked text==


### [Footnotes](https://github.com/markdown-it/markdown-it-footnote)

Footnote 1 link[^first].

Footnote 2 link[^second].

Inline footnote^[Text of inline footnote] definition.

Duplicated footnote reference[^second].

[^first]: Footnote **can have markup**

 and multiple paragraphs.

[^second]: Footnote text.


### [Definition lists](https://github.com/markdown-it/markdown-it-deflist)

Term 1

:   Definition 1
with lazy continuation.

Term 2 with *inline markup*

:   Definition 2

     { some code, part of Definition 2 }

 Third paragraph of definition 2.

_Compact style:_

Term 1
~ Definition 1

Term 2
~ Definition 2a
~ Definition 2b


### [Abbreviations](https://github.com/markdown-it/markdown-it-abbr)

This is HTML abbreviation example.

It converts "HTML", but keep intact partial entries like "xxxHTMLyyy" and so on.

*[HTML]: Hyper Text Markup Language

### [Custom containers](https://github.com/markdown-it/markdown-it-container)

::: warning
*here be dragons*
:::
 '
),(
 '7b70bab4b58d4265b18b7d5859efbb62',
 'anonymous',
    '2023-07-15 14:01:17.777',
    '6edb6033f8ea40858179cd657e9b9c8e',
 'The 12 Principles behind the Agile Manifesto(from agilealliance.org)',
   'MARKDOWN',
 'We are uncovering better ways of developing software by doing it and helping others do it.
Through this work we have come to value:

Individuals and interactions over processes and tools
Working software over comprehensive documentation
Customer collaboration over contract negotiation
Responding to change over following a plan

That is, while there is value in the items on the right, we value the items on the left more.
© 2001, the Agile Manifesto authors
This declaration may be freely copied in any form, but only in its entirety through this notice.

1. Our highest priority is to satisfy the customer through early and continuous delivery of valuable software.

2. Welcome changing requirements, even late in development. Agile processes harness change for the customer’s competitive advantage.

3. Deliver working software frequently, from a couple of weeks to a couple of months, with a preference to the shorter timescale.

4. Business people and developers must work together daily throughout the project.

5. Build projects around motivated individuals. Give them the environment and support they need, and trust them to get the job done.

6. The most efficient and effective method of conveying information to and within a development team is face-to-face conversation.

7. Working software is the primary measure of progress.

8. Agile processes promote sustainable development. The sponsors, developers, and users should be able to maintain a constant pace indefinitely.

9. Continuous attention to technical excellence and good design enhances agility.

10. Simplicity–the art of maximizing the amount of work not done–is essential.

11. The best architectures, requirements, and designs emerge from self-organizing teams.

12. At regular intervals, the team reflects on how to become more effective, then tunes and adjusts its behavior accordingly.'
);

-- core_page
insert into `core_page` (
    `page_id`,
    `name`,
    `content_format`,
    `content`
) values (
             'board',
             'Board Demo',
             'MARKDOWN',
             '# 데모 게시판 최근 게시물 페이지

         데모게시판 최근게시물 위젯을 포함한 페이지

         '
         );

-- page_widget
insert into `core_page_widget` (
    `page_id`,
    `index`,
    `type`,
    `properties`
) values (
             'board',
             0,
             'org.chomookun.arch4j.web.view.board.widget.LatestArticleController',
             'boardId=notice
         pageSize=10'
         ), (
             'board',
             1,
             'org.chomookun.arch4j.web.view.board.widget.LatestArticleController',
             'boardId=anonymous
         pageSize=10'
         ), (
             'board',
             2,
             'org.chomookun.arch4j.web.view.board.widget.LatestArticleController',
             'boardId=member
         pageSize=10'
         );

-- core_git
insert into `core_git` (`git_id`,`name`,`note`,`url`,`branch`, `discussion_enabled`, `discussion_id`) values
    ('arch4j', 'arch4j repository', 'Arch4j Github Repository','https://github.com/chomookun/arch4j.git', null, 'Y', '7eddf209186d487ab58e30a090944d79');

-- core_template
insert into `core_template` (`template_id`,`system_required`,`name`,`format`) values
    ('verification.email','Y','Verification Email','HTML'),
    ('verification.sms','Y','Verification SMS','TEXT');

insert into `core_template_i18n` (`template_id`, `locale`, `subject`, `content`) values
    ('verification.email','en', 'Verification Code: [[${code}]]', 'Verification Code: <span data-th-text="${code}" style="font-weight:bold;"></span>'),
    ('verification.sms','en', 'Verification Code: [[${code}]]', 'Verification Code: [[${code}]]'),
    ('verification.email','ko', '인증코드: [[${code}]]', '인증코드: <span data-th-text="${code}" style="font-weight:bold;"></span>'),
    ('verification.sms','ko', '인증코드: [[${code}]]', '인증코드: [[${code}]]');

-- core_notification
insert into `core_notifier` (`notifier_id`,`system_required`,`name`, `client_type`, `client_properties`) values
    ('email','Y','Email','EMAIL','host=sandbox.smtp.mailtrap.io
port=25
username=987a77fac40349
password=cfad266492f5fa
    '),
    ('slack','N','Slack','SLACK','url=https://hooks.slack.com/services/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
    ');

-- core_verifier
insert into `core_verifier` (`verifier_id`,`system_required`,`name`,`client_type`,`client_properties`,`enabled`) values
    ('totp','Y','TOTP', 'totp', '','Y'),
    ('email','Y','Email', 'notification', 'notifierId=email
templateId=verification.email','Y');

-- core_example
insert into `core_example` (`example_id`,`name`,`number`,`decimal_number`,`date_time`,`date`,`time`,`enabled`,`type`,`code`,`icon`) values
    ('3ff5afd1bf214e51b6c97e99f383804f','James',1, 123.4321, '2025-03-12T12:39:00', '2025-03-12', '21:42:00', 'Y', 'HUMAN', 'STUDENT', 'data:image/png;base64,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'),
    ('6aa5b0579e42404e8e27045a160cc308','Apple',2, 102.0100, null, null, null, 'N', 'FRUIT', 'TEACHER', 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAACXBIWXMAAAsTAAALEwEAmpwYAAAEHklEQVR4nO2XX2gcRRzHp6C+WfFBUZCCiD55M7u3O3NNckm43ZPb5Na7TdNVxD7kxYJV6INNucuDilLQPlhBFBX8g/hiTfaivlYtiChFlOxt72KbJk2NSdO0NbnbtCltb2TuLt72aJPu/dsK94Xfwy47zPczv9/8ZhaAttpqq+kajsXuTejKbuCloqM7Ho8a2l7V0Aw1Fc+ohnaRRTQVz0YN7Yv+sQEFULDlZmMTeuSDpK7QpB55CXilaCq+qKY0ulFEU/Gj6rfqNue4kcE+MbEzcj2pR9YSz/Y94SXA95sBFCEM7W/F0B5bH5fUlXG2+gldeRt4KdXQDtwOQBnC0g/r9+zT+x9aX/1XBp560FOA2HiMv12AUsT3JfW+F0q1r3wD7gSphvazC4Dl/c9HviyWz05lD7gT9OJHsdD7r4ev/zLUTRfU7TQXJnSpL0CzehdN7Q3R195RaHysArF7pH+WAYw8o2xn46kg3L0qES0v4U9sCWfyElmzZUJtiSzbEv5pJSQOzwe5B5piPicHBm2ZLBUn3CBOax30w1fDdMdonD733tNrDOCw3v2oLZNhW8ZnNxu/2C1emMH8wYaat2WS3Gzi6jg10EnfeCtS+FyTaU7Cf7kZezrAF6YEbrQx5iU85NZ8vZGTMLU4RCcFtL8u85fCwjZbxrlWA9jFLPiphdC1CZ/vkZoB8hL52Avzdmkv0DRCNMNxX9VkfjkYvP+/LuFBrIRwESCN0NXfBOG+/0Xt21VxnCsCUBOhXa4B8jL5zGuALM+VsgDhp+4zIOMJrwEm/SUAE8JjNQBsfmg1OzKVDCy5BshL5IrXANb6HoAwVwvAqpfmVypdiG3ifC0AZ7wEOFc+B8oZOOkawJbIUS8B5jr8ToAf3API+JCXANOYdwIccg2Ql4julfm84xArd6FB1wDnFbI1L+PLXgAs9TjqH6HLJwjZ6hqglAU87gXAbKBS/2kIDVCr7DCOtNp8TmLlw1UywHHhmgEoAFtsCZutBJjrFJzl8zvzUDNAKQsk2trVR87V7weN0HKIHGkFwAxxtE6EvgON0mIn5ha7hKZeLc4GK50nDeE/df1K3kyzhHuTrRD74W60+XM9YuXihlDB8vlioBmaJtwRdsVd6BKKh81GtcxaYZZHxTv9XIdA87cAn+8SqOXsOhAmQDM1hblf2UTZsrELvZjmHDfIhaBYucc7gr1jZtm3DP58j0inxErNl1f/AGiFpol/zKoyeLzqmYXFoYKFUOGG9xx7fyOgidAVE6GXQSs1G/APTfLoUrVpp/kZkduThjBiIbh6q+9MCI9N+Hx+4IV+7O29aybAH/xT5OYzHLrGspLh0dUpkf/jjCg+uf5dRhAeNhF614TwRBqhiyaEsyZCX6chVOs+qNpqq6222gJ16F+W3FlCfvUoAAAAAABJRU5ErkJggg=='),
    ('4dda1950bedf43cd9b9cdf7d39475302','Monkey',3, 123.4321, null, null, null, 'Y', 'ANIMAL', 'TEACHER', null),
    ('0c6afae08efe4d3a96fec9f61291c7e4','Orange',1, 123.4321, '2025-03-12T12:39:00', '2025-03-12', '21:42:00', 'Y', 'FRUIT', 'STUDENT', 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAACXBIWXMAAAsTAAALEwEAmpwYAAADjklEQVR4nO1XSWgUWxStqCj+jbgQFDQJ6feq7q0mXa+67NDBIXRHE40RB9TEOHxI8CuIOw2iiFGccEQNTnGIaBJBdKEujOCMiIILP8nn//yI2Bhd6MIpEgW98rpSQWOnOp1UcOOFsynuq3Pu8N67T1F+Wx/N9EGurutDlV9hBofDgiMZXF/+gygV8wXDUwaH/w0O7Z1oERxOBlQ96pkAwfR5tgB4xhgbZmZihsHxlvzmCk2/bWXp6X3lTZMRGgx2GRzvdf2UwYc4OFIo2/9lYXmYKndHafPJAtpSW0hr90Vp8fJcCpv+uL+p4RtDVSekGLFWJjg0uUW3pKyYNh0vpO310xKi6kQBFc0M2v4qfg6oWJKUOJShjxYcLzskRWGkbeVI13YA/X0UKFYH1FoLdL/aotevmqi17SHtv1CSUMDGmgKaUWz1XoCVpacbHFrlgmgQ6XQl0PN6oBcNidF2Pkixp43U3vGGbj2upSOXK2jTiQJavTNCsiw5gRRKIBgbJRg+lQtKo0j/HMceibvjyfUKisVu0seOtz+XSsUbfp9/XLLMpwmGV+SCRflAz870jvhnIO1cYXaRmxxKk9ZcmuCwVC6YOj61yBOWpR5oWZEtIBLES0oym68ogwXDf+WCc+v7Tvw9mmqAcv1Ipiqz4EdXAQEGxZJ8ziRbvRcCJKqW2lkozNEaXAUYHA5JxwMr+5f67nhwwBYw2YD2ZAJapOPDg96RO5A9FW/GTMzoiT9NcOywNKTndd4L+GtG19E9NSF7dnr2SOfQ8ZpcorIEOgVoZQkFWFlZI+LbxRwYAWsW2BkwfLjArQfagwNUgvLptoCAqk/sUYBg+J90kl3reROGOjOgaZluAqrtbegt+d29dv0jJr5T3Czg0wql4+yJ6HrzpYrNf3Ze5zl41lVAXl7eEKcMDeu8IX98BCiESEEVv1qqCq4CpMkulQKmWEDNNf0jl1l09n++iReVXlqa4HhVLirLx35cx0BbK2zysB/ey+mqtwIUeSg5x3JpBKn5WIqR1wFtWGw3XlDFL9kcJympmunTmdMPkSBS7Wr7x8nI5bw4P8+OfLwGn4UPZ6VM3u14bnSmmulhjKe1cbvdXLGzEM/OnT1A1asgPkE5viHAtgBDU/HCBMcpycbyrtFLla8hrAqPHTtc8dgGyWnWYLhbPkzkq0gw+GRweCk4PjIYng5wmGuNsf7wmnhA7Rvzmk+hUCbmxAAAAABJRU5ErkJggg=='),
    ('4ef9061d3c924cae8ddca8578e4114cc','Tom',1, 123.4321, '2025-03-12T12:39:00', '2025-03-12', '21:42:00', 'Y', 'HUMAN', 'STUDENT', null),
    ('025336d77c1942a7be2f11ee945230e1','Nobody',1, 123.4321, '2025-03-12T12:39:00', '2025-03-12', '21:42:00', 'Y', null, 'STUDENT', null),
    ('c110e9d7bb4a4c17be921e10ff4bf3bb','Bear',1, 123.4321, '2025-03-12T12:39:00', '2025-03-12', '21:42:00', 'N', 'HUMAN', 'STUDENT', 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB4AAAAeCAYAAAA7MK6iAAAACXBIWXMAAAsTAAALEwEAmpwYAAAEs0lEQVR4nO1WWVATZxxP24e+9KUvvZ771I5tbfZIIEhspQYIYTdhQcIR5FDklMNB2E02BiQgAlpRBg1hNCgdnWmnPugMRQr1KoyiTrXvilinh5VCKAadf+e/Lbl2w9Bin9pv5jezs9/v+P7ffseqVP/pJnLvvMKz5AGeIX7hGWLRzlDnG9Po1/+uD2pQix7oJTBkN3orkisNb7/MM8Rtd7bOP1CeAoPVadCVrw/wDDHdxKrfXG0oclGDWvRAr9Zs3Tx6Y4ZcwJAdGHqqxgThaLXGP21KJyAMz0QLdZ9nyUoEPuO7cA5qon3arDq/wJLtsmCBIX/E0YWTD279CNpyE2DKVw7fn94p4fZnVfDNkULYX6Cf2781cX78SBF8N1QV7EcualAb7jVQkQwCQz6UV5xO+H3VxiDxcMkmaM2JDA0HDgCh1Ica1KLHsp+v2gg8Q87LgnmWGOm06QNI8pYng5PTwLeebYrGq8GEp1Ty6C8zSMGdNn1AYMhhWXB9CvkGz5B3u236J65MLQx35f/j0GUMd+aDi9NCN4ay5D3MiL0NzNRcf23qmkOX4alJAbuZ8u8yrX8r9lYwqj8ULfTCrZOVEWJXXR4kbEoHV23sWYjFuXmyApwctdCUTn4QM1jk6LEvWrKeRZsmJJmA0qfBhk/SYwavxPm8mXsmZtCjiqFCunodVqu0UrEKNG6uW6HiFTi43USOWhDSyHfl1VpozxBvXnpe3zYap3h2ycHRfRGhHMe9JLDk/OTAjn8lVNpe3lJcZL9hVmgPM2qdOy9hVklw53Q1nHVvgdJkGqx6ArIT1Yqw6tWwyxwPZ9uyJY2SV2uubnY3Q2pD35chRF+DKaBE9tYboacsCYb2WqHIEAd8Tho4bWwE6jgDFCVrYcJXDj07kmCg3qgYfKLBFOAZUggGOznN8PmOXBlx5KANuoo3wuMJF/in9sKZfflQuFkDddxmKdCRz0AV+zEUG7Rw58t6iYPcrqKNkjba71xHDp5m50LBGfT0laMlMmJflQGuerdJhsu4caYGypkE2J6qkwbhLk2FH8YdERzUoDba73JfCTgt9N3QVLPU3NQJ+WXgztXBzAU+whQxd70FRo+VBquMxv3RJnDnJcj8rh8vA7uZnA2dWAyxdHOwQka0mymYnfxzmqMxM8pLUOp7POkCh4WS+d0arADMCgY7Mqje9oLE+ejwVms8PPha2fyqd7sExUFd4HEFR3jd8FXAvoLEeWcG3Rs6PETVi3YL9WlzltY/frgwSMbLYryvSGb80yUHtNs2QFueDh6OC7L+sd5C8NaFLpqxnkJwZWn9ooU6gFmy02u3iTDZzdSjoztTfr92vAwu9hZBa048PLriDJpe85VBS3Z88DfHtSUOLnlKgv3IRc3F3mJAj2PVyU/sFurXJvP6tJiXhBSeuu5Vh4Xutpuphf7alMX+WqO0N3++LCpOaziQc6gsCbx1RvDUpC4KLLkgZtBd6KlabfvrbnY4LPSDPZnaAFY20lMA975qjFhw+Dw93Agjh2zgyooD5Dos1AxvJgWRfe811RraC3iXNpqItj2ZmmlnpuapwJLBqcZnfOfkNNPIaWCp99cS9n9TPe/2BwyiCdBkE9V/AAAAAElFTkSuQmCC'),
    ('b3c113343f174b2fb6f90a511e813ceb','Camel',1, 123.4321, '2025-03-12T12:39:00', '2025-03-12', '21:42:00', 'Y', 'HUMAN', 'STUDENT', null),
    ('3c2bf04cf2134ecb8eb51ad2f02608fc','Banana',1, 123.4321, '2025-03-12T12:39:00', '2025-03-12', '21:42:00', 'Y', 'FRUIT', 'STUDENT', 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAACXBIWXMAAAsTAAALEwEAmpwYAAADr0lEQVR4nO2YW0gUYRiG/45QXRQEFXhTkbFtZrGTadnBNSuik1SWhWZ0hOhAEJ1xm5EiiKCCqKAutIvoppsONxVqiLYzsyd3XdPUfwwjiojQshLzjZlcc21m3L2Y3VnYB7775/3n+19mhpAkSZLEjfv3ZllvXZ/+8kpZSndFRWpPbW3WOJIo3L45o+Li2Wl93KmpKC9PRRXP4EXt/BSSKFQ6mWuy9OCpFhkbSRREkRlT5bTVDA5QKTDHSSJRJTLzqnhb70AIJ/MaICNIIlHJ28rD1khgDpJEolJgcsLvgq1XXqWEeRLPRWbi0Mssj0vM6GwXs5cSs/PGlx+2QoOn1smgw5uznZiVZv/21U5xRV9bYAfeBYtR58pGdb98K5+Or7wVH8XMb0DBKGJG2gKFfC8tBSROmc6W46gRFiHIzwd4y78JbDFnvXa3nvoakg9Ng2cVunhreADvSh8xG6COSUPllacQLAqXl8eV9Y2YDVDWrhYAb/aoBGB6idkA5e6qBmjY+n8Afi6ImUALlwqJ7VEN4LWrBEjrI2YClH2oKk9LASFNbYV6iFmAxG1VlZc4oGmfyulbAPeyDmIG0O6YCYn9rzoRGs9y9QC+NY/MUps+Tfm3h9Xl+TlAfQETX/l3V8dBYqs15SkLuBerB/Bkf46vfMelybryEgcEd2icvgXwbzoZP3nKzQFlW3Tl3x4Dhr46DLRPZld8xIERaOcOgHJduvJtZwCR0d59/+a9sZeXymaAsi91xZW9Pw+4s7RXx2NvjK34e8d4UPYCKNsdkbxnqba8i+mBpyA2/4gAx0hIXBEo2zGsuLI2p7Ubh+9fnYb8PbGRby/LA+VcEYkrF/YIIC7QkbcAvrUPjBeXynIhsXURi8vTWKzdNnzowyWPN1a81TEblH0clbjcNL48fXFeubRNwIrRxu055c5pvgKrXtQLQGMJIKRHIJ9joPyHKxMgcc+iOvXmQ/oVGbY2dt6wvw7KOwzlXkV84k379esxbKyAb91dQ8QHAlC2fHjxUqBxF+BaFKG4BRAX/oJ/Y6HxFalbiUcB//rIdnxwx3tzA2gpmmKovIxqv7eeBIKF0Z12aNyZ3xHYUEJixfmSVd8DT4/2S+8EPMv+nmC04uLCn6hfdweBgrEklpzYZqfFOZnRC8sjWOXAn1C//rJh9TgcN05s2h1dAFl6yRf41jyN+ydgCNTnF8GT64Yr4weEtD5FUkj/rbwturM64bU3w7/6CfwbzsbkYiZJkoQM5Q9jBjRvwCLkVAAAAABJRU5ErkJggg=='),
    ('a0999014a3e94bbb8782d7ebe5ba8f15','Dog',1, 123.4321, '2025-03-12T12:39:00', '2025-03-12', '21:42:00', 'N', 'HUMAN', null, null),
    ('9e81c602ea42407fa2d351f25a9cc9d6','Kiwi',1, 123.4321, '2025-03-12T12:39:00', '2025-03-12', '21:42:00', 'Y', 'HUMAN', 'STUDENT', null),
    ('6cce6e9b35534b759a5299d5931b8f4a','Pig',1, 123.4321, '2025-03-12T12:39:00', '2025-03-12', '21:42:00', 'Y', 'ANIMAL', 'STUDENT', 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAACXBIWXMAAAsTAAALEwEAmpwYAAAEHklEQVR4nO2XX2gcRRzHp6C+WfFBUZCCiD55M7u3O3NNckm43ZPb5Na7TdNVxD7kxYJV6INNucuDilLQPlhBFBX8g/hiTfaivlYtiChFlOxt72KbJk2NSdO0NbnbtCltb2TuLt72aJPu/dsK94Xfwy47zPczv9/8ZhaAttpqq+kajsXuTejKbuCloqM7Ho8a2l7V0Aw1Fc+ohnaRRTQVz0YN7Yv+sQEFULDlZmMTeuSDpK7QpB55CXilaCq+qKY0ulFEU/Gj6rfqNue4kcE+MbEzcj2pR9YSz/Y94SXA95sBFCEM7W/F0B5bH5fUlXG2+gldeRt4KdXQDtwOQBnC0g/r9+zT+x9aX/1XBp560FOA2HiMv12AUsT3JfW+F0q1r3wD7gSphvazC4Dl/c9HviyWz05lD7gT9OJHsdD7r4ev/zLUTRfU7TQXJnSpL0CzehdN7Q3R195RaHysArF7pH+WAYw8o2xn46kg3L0qES0v4U9sCWfyElmzZUJtiSzbEv5pJSQOzwe5B5piPicHBm2ZLBUn3CBOax30w1fDdMdonD733tNrDOCw3v2oLZNhW8ZnNxu/2C1emMH8wYaat2WS3Gzi6jg10EnfeCtS+FyTaU7Cf7kZezrAF6YEbrQx5iU85NZ8vZGTMLU4RCcFtL8u85fCwjZbxrlWA9jFLPiphdC1CZ/vkZoB8hL52Avzdmkv0DRCNMNxX9VkfjkYvP+/LuFBrIRwESCN0NXfBOG+/0Xt21VxnCsCUBOhXa4B8jL5zGuALM+VsgDhp+4zIOMJrwEm/SUAE8JjNQBsfmg1OzKVDCy5BshL5IrXANb6HoAwVwvAqpfmVypdiG3ifC0AZ7wEOFc+B8oZOOkawJbIUS8B5jr8ToAf3API+JCXANOYdwIccg2Ql4julfm84xArd6FB1wDnFbI1L+PLXgAs9TjqH6HLJwjZ6hqglAU87gXAbKBS/2kIDVCr7DCOtNp8TmLlw1UywHHhmgEoAFtsCZutBJjrFJzl8zvzUDNAKQsk2trVR87V7weN0HKIHGkFwAxxtE6EvgON0mIn5ha7hKZeLc4GK50nDeE/df1K3kyzhHuTrRD74W60+XM9YuXihlDB8vlioBmaJtwRdsVd6BKKh81GtcxaYZZHxTv9XIdA87cAn+8SqOXsOhAmQDM1hblf2UTZsrELvZjmHDfIhaBYucc7gr1jZtm3DP58j0inxErNl1f/AGiFpol/zKoyeLzqmYXFoYKFUOGG9xx7fyOgidAVE6GXQSs1G/APTfLoUrVpp/kZkduThjBiIbh6q+9MCI9N+Hx+4IV+7O29aybAH/xT5OYzHLrGspLh0dUpkf/jjCg+uf5dRhAeNhF614TwRBqhiyaEsyZCX6chVOs+qNpqq6222gJ16F+W3FlCfvUoAAAAAABJRU5ErkJggg=='),
    ('c84254c0c9154294a41bac11f14be18b','Cow',1, 123.4321, '2025-03-12T12:39:00', '2025-03-12', '21:42:00', 'Y', 'HUMAN', 'STUDENT', null),
    ('9c79ab6d55dc47a8859a75537bd92d52','Lemon',1, 123.4321, '2025-03-12T12:39:00', '2025-03-12', '21:42:00', 'N', 'FRUIT', 'TEACHER', null),
    ('1fa26fb343b3415f834995efaedb7161','Jack',1, 123.4321, '2025-03-12T12:39:00', '2025-03-12', '21:42:00', 'Y', 'HUMAN', 'STUDENT', 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAACXBIWXMAAAsTAAALEwEAmpwYAAAEGklEQVR4nO2YbWxTVRzGb4fSFqdMRrp7xwi5G0oYhoRtuCAqRo1mKhjRQXyJoGBg3g5iMDHwQRE/IBpjNL5F+SZfnMSEl22e05glZluJxAixSLudM4FtLmy+8LKwzdE+5oy70K1su/f23tvF9EmepLnt+f9/z3lrWknKKqusbJFWiPygjGpNxpdBGa2aAqYp6NfNNBkt+nvVrxVhjjRdpMmo0BR8qymIBxXAoK8GFdRtU1CeMfDaeSgKKqg3AT2Rj2wpwjxX4YOFWB+U8XcyyJaCf7E2rw/3z2pH2c0nsWTGcSz0tI5YvBbPxHviM+KzyWM1GX+9WoB1rsBrCt5Ibv7i3H7c629DsdQCVWo25GKpGZXe3/D83EtjV0PGu07D7xlttjkwhErvKcPQE7nSe2qkVlKQt52Bl7FhtInYBnfmHEsbXtUtaomaev2EpuA5W+FrAigOKrgsGlTd2mkbuDrOD+eeuXYmFFwSl4RtATQZIVH4kdyzjsGrukUPfSUO2gOv4FFR8Mm8847Dq7qfyusdCVErY5Uds09eDgxgoSfsWoA7POHRg/1NWvA1hZgvDtVyb8Q1eFX3Ct9pcRaGtxYgYH32C6Ctn/OP6/Cq7mfzL4gQmy0H2KHi0N03mP13XuoEp7DVezaeS+lzjy+K10twwHKAmgUDvTeamdAX/bYHIJ/1p/Qp8bSgtmSoy3KANbd3x8cXfbz0pO3wXHfVohOpN1J+z7Al+LdKMXOlPzqmWLGnGd+9f8GxAAf3pZ63B3LbE9YCSMhZ6v15TLHtq7lj8Fz3tifYmJ7l/hOwFKCpCTct9l+/+9dVRBBrSDgeINaQwDNl1y+OpbnHAMBjOsCOF3puGS3yykMxROvjKc1avx7E2mW/otQXxtNlEYQPDE4JaGTM6aNxbHowNtJ7sS8M1GGGhTVATmXgp8T+XX0TwgiQ5OUWMzdVADNj9u/sxQrleNw0eqQOMznFe9H6+PBkMMlbTLjUH54ygNkx0WsM+wST4QBigJH9KraA2RWwMoYLExO/1hjFH0aKiv0sAMSsVpcbPwNmx3AKMIIe4wEIuu26VWwzgfFvZE6wdxoG2GvuEIsQBF3TALxLsJg6xOO2UyiD8I1SuuIEuzMVgFG8mXaAjh+wgFFcnayR1b8V+eTw8TMEatoB9FWoz0CAw5Jd6qBYxQgSbgVgolcj7pPsFCP4yrUVIPhcsls8hNmcotOFm+dsWwNuk5wQI7iLE/zpIHxfeyOWSE6Kh7CcU1x0IMBFRlEhuaGORixiFL/YCB9xfObH6/cm+DjBh5xiKA3wQUbxgaglZUrt32M+o/iUEQwYBWcEVxjFJ20NNv6Fnq66D2MWo3iME3zMKH5kBDFOcFmYUUTFM07xESOoOtcKf6Z5s8rq/6L/AP2YVak3nx26AAAAAElFTkSuQmCC'),
    ('9e2dddabad9b47eeb2f889d5c4105647','Fox',1, 123.4321, '2025-03-12T12:39:00', '2025-03-12', '21:42:00', 'Y', 'ANIMAL', 'TEACHER', null),
    ('0b0f1fb6458949cba7c2f42cac4cbd7a','Cherry',1, 123.4321, '2025-03-12T12:39:00', '2025-03-12', '21:42:00', 'N', 'HUMAN', 'STUDENT', 'data:image/png;base64,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'),
    ('3f2adc3f3954449b88a0482f461a72e6','Olivia',1, 123.4321, '2025-03-12T12:39:00', '2025-03-12', '21:42:00', 'Y', 'HUMAN', 'STUDENT', null),
    ('b9b61b5fa65d48cbaa1893ff67d31c82','Cat',1, 123.4321, '2025-03-12T12:39:00', '2025-03-12', '21:42:00', 'Y', 'HUMAN', 'STUDENT', null),
    ('f709b892f7df4d2393601079b8634de7','Harry',1, 123.4321, '2025-03-12T12:39:00', '2025-03-12', '21:42:00', 'Y', 'HUMAN', 'STUDENT', null);

-- core_example_item
insert into `core_example_item` (`example_id`, `item_id`, `sort`, `name`) values
    ('3ff5afd1bf214e51b6c97e99f383804f', 'Item1', 1, 'Item 1'),
    ('3ff5afd1bf214e51b6c97e99f383804f', 'Item2', 2, 'Item 2');


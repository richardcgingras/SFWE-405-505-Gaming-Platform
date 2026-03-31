# SFWE-405-505-Gaming-Platform
SFWE 405/505 Semester Project

# Temp Auth Notes:
## Login
``` bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"johna","password":"password"}'
```
Example response:
```
{"accessToken":"eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiIxMDEiLCJpYXQiOjE3NzQ5MTczMzUsImV4cCI6MTc3NTAwMzczNX0.VTnnIUtDnB2i3UgUhzixa2Bv4XToNbso6QRYzUVaKdpOF8CNk3oQ0J_xBq0f08XD","tokenType":"Bearer"}
```

## Use other APIs
``` bash
curl -X GET http://localhost:8080/api/user-profiles -H "Authorization: Bearer <token-from-login>"
```
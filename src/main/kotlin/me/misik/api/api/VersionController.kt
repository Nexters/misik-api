package me.misik.api.api;

import me.misik.api.domain.response.WebviewHomeResponse
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VersionController {

    @GetMapping("webview/home")
    fun getWebviewHome(
    ) : WebviewHomeResponse = WebviewHomeResponse("https://misik-web.vercel.app/")
}

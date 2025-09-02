package com.skwmium.ragsample.controller

import com.skwmium.ragsample.service.ChatService
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class ChatController(
    private val chatService: ChatService,
) {

    @GetMapping("/")
    fun index(model: ModelMap): String {
        model.addAttribute("chats", chatService.getAllChats())
        return "chat"
    }

    @GetMapping("/chat/{chatId}")
    fun showChat(@PathVariable chatId: Long, model: ModelMap): String {
        model.addAttribute("chats", chatService.getAllChats())
        model.addAttribute("chat", chatService.getChat(chatId))
        return "chat"
    }

    @PostMapping("/chat/new")
    fun createChat(@RequestParam title: String): String {
        val chatEntity = chatService.createChat(title)
        return "redirect:/chat/${chatEntity.id}"
    }

    @PostMapping("/chat/{chatId}/delete")
    fun deleteChat(@PathVariable chatId: Long): String {
        chatService.deleteChat(chatId)
        return "redirect:/"
    }
}
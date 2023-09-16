
import com.example.demo.controller.ArticleController
import com.example.demo.controller.AuthController
import com.example.demo.controller.CommentController
import com.example.demo.controller.UserController
import com.example.demo.dto.request.*
import com.example.demo.dto.response.LoginResponse
import com.example.demo.exception.UnauthorizedUserException
import com.example.demo.model.Article
import com.example.demo.service.ArticleService
import com.example.demo.service.CommentService
import com.example.demo.service.UserService
import org.springframework.http.HttpStatus
import java.util.*
import javax.naming.AuthenticationException
import kotlin.system.exitProcess

class TerminalUI(
    private val authController: AuthController,
    private val userController: UserController,
    private val articleController: ArticleController,
    private val commentController: CommentController,
) {
    private val scanner = Scanner(System.`in`)

    private var loggedInUser: LoginResponse? = null  // 현재 로그인하고 있는 유저 정보를 저장할 변수

    fun start() {
        print(  "██████╗   ██        ██████╗    ██████╗\n" +
                "██╔══██╗  ██       ██╔═══██╗  ██╔════╝\n" +
                "██████╔╝  ██       ██║   ██║  ██║  ███╗\n" +
                "██╔══██╗  ██       ██║   ██║  ██║   ██║\n" +
                "██████╔╝  ███████╗ ╚██████╔╝  ╚██████╔╝\n" +
                " ╚════╝   ╚══════╝  ╚═════╝    ╚═════╝   made by sihlee\n")
        while (true)
        {
            if (loggedInUser == null)
                printMenuBeforeLogin()
            else
                printMenuAfterLogin()
        }
    }

    private fun login() {
        print("Enter email: ")
        val email = scanner.nextLine().trim()

        print("Enter password: ")
        val password = scanner.nextLine()

        if (email.isBlank() || password.isBlank()) {
            println("Email and password should not be empty!")
            return
        }

        try {
            val user = authController.login(LoginRequest(email, password))
            println("Login successful!")
            loggedInUser = user
            println("Hello, ${loggedInUser!!.username} 👋")
        } catch (e: UnauthorizedUserException) {
            println(e.message)
        }
    }


    private fun signup() {
        print("Enter email: ")
        val email = scanner.nextLine().trim()
        print("Enter password: ")
        val password = scanner.nextLine().trim()

        authController.signup(SignupRequest(email, password, email))
    }

    private fun printMenuBeforeLogin() {
        print("""
        ╭────────────────────────╮
        │                        │
        │    Welcome to          │
        │    DemoBlog Terminal   │
        │                        │
        │    Enter               │
        │    [1] to login        │
        │    [2] to signup       │
        │    [3] to exit         │
        │                        │
        ╰────────────────────────╯
        [ENTER] >> """)
        when (scanner.nextLine().trim()) {
            "1" -> login()
            "2" -> signup()
            "3" -> {
                println("Goodbye!")
                exitProcess(0)
            }
            else -> println("Invalid choice. Please try again.")
        }
    }
    private fun printMenuAfterLogin() {
        print("""
        ╭─────────────────────────╮
        │                         │
        │  Welcome back!          │
        │                         │
        │  You can                │
        │  [1] see all articles   │
        │  [2] go to your blog    │
        │  [3] logout             │
        │  [4] exit program       │
        │                         │
        ╰─────────────────────────╯
        [ENTER] >> """)

        when (scanner.nextLine().trim()) {
            "1" -> displayAllArticles()
            "2" -> printMyPage()
            "3" -> {
                println("Logged out successfully!")
                loggedInUser = null
            }
            "4" -> {
                println("Goodbye!")
                exitProcess(0)
            }
            else -> println("Invalid choice. Please try again.")
        }
    }
    private fun printMyPage() {
        print("""
        ╭─────────────────────────╮
        │                         │
        │  MY PAGE                │
        │                         │
        │  [1] See my articles    │
        │  [2] Create new article │
        │  [3] Update an article  │
        │  [4] Delete an article  │
        │  [5] Back to main menu  │
        │                         │
        ╰─────────────────────────╯
        [ENTER] >> """)

        when (scanner.nextLine().trim()) {
            "1" -> displayMyArticles()
            "2" -> createArticle()
            "3" -> updateArticle()
            "4" -> deleteArticle()
            "5" -> printMenuAfterLogin()
            else -> println("Invalid choice. Please try again.")
        }
    }

    private fun displayAllArticles() {
        val articles = articleController.readAllArticles()

        if (articles.isEmpty()) {
            println("📭 No articles found.")
            return
        }

        println("📜 All articles 📜")
        articles.forEach { article ->
            println("[${article.id}] ${article.title}")
        }

        println("\nEnter an article ID to view, or type 'back' to go back:")
        val choice = scanner.nextLine().trim()

        if (choice == "back") {
            return
        }

        val selectedArticleId = choice.toLongOrNull()
        if (selectedArticleId != null && articles.any { it.id == selectedArticleId }) {
            readArticle(selectedArticleId)
        } else {
            println("Invalid choice.")
        }
    }
    private fun displayMyArticles() {
        val articles = articleController.readSpecificArticles(loggedInUser!!.email)

        if (articles.isEmpty()) {
            println("📭 No articles found.")
            return
        }

        println("📜 My articles 📜")
        articles.forEach { article ->
            println("[${article.id}] ${article.title}")
        }

        println("\nEnter an article ID to view, or type 'back' to go back:")
        val choice = scanner.nextLine().trim()

        if (choice == "back") {
            return
        }

        val selectedArticleId = choice.toLongOrNull()
        if (selectedArticleId != null && articles.any { it.id == selectedArticleId }) {
            readArticle(selectedArticleId)
        } else {
            println("Invalid choice.")
        }
    }
    private fun displayArticleInformation(article: Article) {
        println("═════════════════════════════════════════════════")
        println("\u001B[34mArticle No: ${article.id}\u001B[0m") // 파란색으로 제목 출력
        println("\u001B[34mTitle: ${article.title}\u001B[0m") // 파란색으로 제목 출력
        println("\u001B[32m--------------------------------------------------\u001B[0m") // 녹색 구분선
        println("Content: ${article.content}")
        println("\u001B[32m--------------------------------------------------\u001B[0m") // 녹색 구분선
        println("Written At: ${article.createdAt}")
        println("Last Updated At: ${article.updatedAt}")
    }
    private fun displayComments(articleId: Long) {
        val comments = commentController.getCommentsByArticleId(articleId)
        if (comments.isNotEmpty()) {
            println("🗣️ Comments 🗣️")
            comments.forEach { comment ->
                println("[${comment.id}] [${comment.user.username} at ${comment.createdAt}] ${comment.content}")
            }
        } else {
            println("No comments yet.")
        }
    }

    private fun displayActions(article: Article) {
        val actionsList = mutableListOf("[1] Add Comment", "[2] Delete comment")

        val isUserArticleAuthor = loggedInUser?.email == article.user.email

        // Check if the logged-in user is the author of the article
        if (isUserArticleAuthor) {
            actionsList.add("[3] Edit Article")
            actionsList.add("[4] Delete Article")
        }

        actionsList.add("[0] Go Back")

        // Display the options in one line
            println("Actions: ${actionsList.joinToString(" | ")}")
            print(">> ")
            val actionChoice = readLine()?.toIntOrNull()

            if (actionChoice == 0) {
                println("Returning to the previous menu...")
            }
            else if (!isUserArticleAuthor && (actionChoice == 3 || actionChoice == 4)) {
                println("Invalid choice!")
            }
            else if (actionChoice == null || actionChoice !in 1..4) {
                println("Invalid choice!")
            }
            else handleUserAction(actionChoice, article)
    }
    private fun handleUserAction(actionChoice: Int?, article: Article) {
        when(actionChoice) {
            1 -> createComment(article.id)
            2 -> {
                // Delete Comment logic
                print("Enter the comment ID to delete: ")
                val commentId = readLine()?.toLongOrNull()
                if (commentId != null)
                    deleteComment(article.id, commentId)
            }
            3 -> updateArticle()
            4 -> deleteArticle()
        }
    }

    private fun createArticle() {
        println("📝 Create a new article 📝")

        // 토큰 발급 대신 비밀번호 재입력 방식으로 보안 유지
        print("Enter password for verification:")
        val password = scanner.nextLine().trim()

        // Check if the password matches with the loggedInUser's password
        if (!verifyUserLogin(loggedInUser!!.email, password)) {
    return
}

        // 제목 입력
        print("Enter article title: ")
        val title = scanner.nextLine().trim()

        // 내용 입력
        println("Enter article content. 🔔Type 'END' on a new line to finish🔔:")
        val contentLines = mutableListOf<String>()
        var line: String?
        do {
            line = scanner.nextLine().trim()
            if (line != "END") {
                contentLines.add(line)
            }
        } while (line != "END")
        val content = contentLines.joinToString("\n")

        if (!isValidArticle(title, content)) {
            println("❌ Failed to create the article. Please check title and content.")
            return
        }

        // CreateArticleRequest DTO 생성
        val createRequest = CreateArticleRequest(loggedInUser!!.email, password, title, content)

        // 게시글 생성 시도
        try {
            articleController.createArticle(createRequest)
            println("✅ Article created successfully!")
        } catch (ex: Exception) {
            println("❌ Failed to create the article: ${ex.message}. Please try again.")
        }
    }

    private fun updateArticle() {
        println("📝 Update an article 📝")

        // 게시글 ID 입력
        print("Enter the article ID you want to update: ")
        val articleId = scanner.nextLine().trim().toLongOrNull()
        if (articleId == null) {
            println("Invalid article ID. Returning to menu.")
            return
        }

        // 비밀번호 재확인 (게시글 작성자만 수정 가능하도록)
        // 토큰 발급 대신 비밀번호 재입력 방식으로 보안 유지
        print("Enter password for verification: ")
        val password = scanner.nextLine().trim()

        // Check if the password matches with the loggedInUser's password
        if (!verifyUserLogin(loggedInUser!!.email, password)) {
    return
}

        // 제목 입력
        print("Enter new article title (or press enter to keep the current): ")
        val title = scanner.nextLine().trim()

        // 내용 입력
        println("Enter new article content (or press enter to keep the current). 🔔Press 'END' to finish🔔:")
        val contentLines = mutableListOf<String>()
        while (true) {
            val line = scanner.nextLine().trim()
            if (line == "END") break
            contentLines.add(line)
        }
        val content = contentLines.joinToString("\n")

        if (!isValidArticle(title, content)) {
            println("❌ Failed to update the article. Please ensure you provide valid title and content.")
            return
        }

        // UpdateArticleRequest DTO 생성
        val updateRequest = UpdateArticleRequest(loggedInUser!!.email, password, title, content)

        // 게시글 수정 시도
        try {
            articleController.updateArticle(articleId, updateRequest)
            println("✅ Article updated successfully!")
        } catch (ex: Exception) {
            println("❌ Failed to update the article. Please try again. Error: ${ex.message}")
        }
    }
    private fun readArticle(articleId: Long) {
        val article = articleController.readArticle(articleId)
        displayArticleInformation(article)
        displayComments(articleId)
        displayActions(article)
    }
    private fun deleteArticle() {
        println("📝 Delete an existing article 📝")

        // 게시글 ID 입력
        print("Enter the article ID you want to delete: ")
        val articleId = scanner.nextLine().trim().toLongOrNull()
        if (articleId == null) {
            println("Invalid article ID. Returning to menu.")
            return
        }

        // 비밀번호 재확인 (게시글 작성자만 삭제 가능하도록)
        print("Enter password for verification: ")
        val password = scanner.nextLine().trim()

        // Check if the password matches with the loggedInUser's password
        if (!verifyUserLogin(loggedInUser!!.email, password)) {
    return
}

        // DeleteArticleRequest DTO 생성 (이 경우 비밀번호가 필요한지는 구체적인 요구사항에 따라 다를 수 있습니다.)
        val deleteRequest = DeleteArticleRequest(loggedInUser!!.email, password)

        // 게시글 삭제 시도
        try {
            articleController.deleteArticle(articleId, deleteRequest)
            println("✅ Article deleted successfully!")
        } catch (ex: Exception) {
            println("❌ Failed to delete the article. Please try again. Error: ${ex.message}")
        }
    }

    private fun createComment(articleId: Long) {
        println("📝 Create a new comment 📝")

        // 토큰 발급 대신 비밀번호 재입력 방식으로 보안 유지
        print("Enter password for verification: ")
        val password = scanner.nextLine().trim()

        // Check if the password matches with the loggedInUser's password
        if (!verifyUserLogin(loggedInUser!!.email, password)) {
    return
}

        // 제목 입력
        print("Write your comment: ")
        val content = scanner.nextLine().trim()

        if (!isValidComment(content))
            println("❌ Failed to create the comment. Please check content.")

        // CreateCommentRequest DTO 생성
        val createRequest = CreateCommentRequest(loggedInUser!!.email, password, content)

        // 댓글 생성 시도
        try {
            commentController.createComment(articleId, createRequest)
            println("✅ Comment created successfully!")
        } catch (ex: Exception) {
            println("❌ Failed to create the comment. Please try again.")
        }
    }
    private fun updateComment(articleId: Long, commentId: Long) {
        println("📝 Update a comment 📝")

        // 비밀번호 재확인 (댓글 작성자만 수정 가능하도록)
        print("Enter password for verification: ")
        val password = scanner.nextLine().trim()

        // Check if the password matches with the loggedInUser's password
        if (!verifyUserLogin(loggedInUser!!.email, password)) {
    return
}

        // 새로운 댓글 내용 입력
        print("Enter new comment content: ")
        val content = scanner.nextLine().trim()

        if (!isValidComment(content)) {
            println("❌ Failed to update the comment. Please ensure you provide valid content.")
            return
        }

        // UpdateCommentRequest DTO 생성
        val updateRequest = UpdateCommentRequest(loggedInUser!!.email, password, content)

        // 댓글 수정 시도
        try {
            commentController.updateComment(articleId, commentId, updateRequest)
            println("✅ Comment updated successfully!")
        } catch (ex: Exception) {
            println("❌ Failed to update the comment. Please try again. Error: ${ex.message}")
        }
    }
    private fun deleteComment(articleId: Long, commentId: Long) {
        println("📝 Delete a comment 📝")

        // 비밀번호 재확인 (댓글 작성자만 삭제 가능하도록)
        print("Enter password for verification: ")
        val password = scanner.nextLine().trim()

        // Check if the password matches with the loggedInUser's password
        if (!verifyUserLogin(loggedInUser!!.email, password)) {
    return
}

        // DeleteCommentRequest DTO 생성 (이 경우 비밀번호가 필요한지는 구체적인 요구사항에 따라 다를 수 있습니다.)
        val deleteRequest = DeleteCommentRequest(loggedInUser!!.email, password)

        // 댓글 삭제 시도
        try {
            commentController.deleteComment(articleId, commentId, deleteRequest)
            println("✅ Comment deleted successfully!")
        } catch (ex: Exception) {
            println("❌ Failed to delete the comment. Please try again. Error: ${ex.message}")
        }
    }

    private fun isValidArticle(title: String?, content: String?): Boolean {
        // 제목과 내용이 null이거나 공백만 있는 경우 false를 반환합니다.
        return !(title.isNullOrBlank() || content.isNullOrBlank())
    }
    private fun isValidComment(content: String?): Boolean {
        // 내용이 null이거나 공백만 있는 경우 false를 반환합니다.
        return !(content.isNullOrBlank())
    }
    private fun verifyUserLogin(email: String, password: String): Boolean {
        return try {
            authController.login(LoginRequest(email, password))
            true
        } catch (ex: UnauthorizedUserException) {
            println("Please check your password and try again.")
            false
        }
    }




}

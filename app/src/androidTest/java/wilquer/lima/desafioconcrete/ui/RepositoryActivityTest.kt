package wilquer.lima.desafioconcrete.ui

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.hamcrest.CoreMatchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import wilquer.lima.desafioconcrete.R
import wilquer.lima.desafioconcrete.RecyclerViewMatcher
import wilquer.lima.desafioconcrete.data.model.Owner
import wilquer.lima.desafioconcrete.data.model.Repository
import wilquer.lima.desafioconcrete.ui.repository.RepositoryActivity
import wilquer.lima.desafioconcrete.util.Constants

@RunWith(AndroidJUnit4::class)
class RepositoryActivityTest {

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(RepositoryActivity::class.java)

    @Test
    fun whenLoadRepositoryList_shouldLoadRecyclerView() {
        Thread.sleep(2500)

        onView(
            RecyclerViewMatcher(R.id.recycleRepositories)
                .atPosition(0,
                    R.id.txt_repositoryName
                )
        ).check(matches(withText(mockRepository().name)))

        onView(
            RecyclerViewMatcher(R.id.recycleRepositories)
                .atPosition(0, R.id.txt_username)
        ).check(matches(withText(mockRepository().owner.login)))

        onView(
            RecyclerViewMatcher(R.id.recycleRepositories)
                .atPosition(0, R.id.txt_fullName)
        ).check(matches(withText(mockRepository().full_name)))

        onView(
            RecyclerViewMatcher(R.id.recycleRepositories)
                .atPosition(0, R.id.txt_fork)
        ).check(matches(withText(mockRepository().forks.toString())))

        onView(
            RecyclerViewMatcher(R.id.recycleRepositories)
                .atPosition(0, R.id.txt_star)
        ).check(matches(withText(mockRepository().stargazers_count.toString())))
    }

    @Test
    fun whenClickARepository_shouldOpenPullRequestActivity() {
        Intents.init()
        Thread.sleep(2500)

        val matcher = CoreMatchers.allOf(
            IntentMatchers.hasExtra(Constants.REPOSITORY_NAME, mockRepository().name),
            IntentMatchers.hasExtra(Constants.CREATOR, mockRepository().owner.login)
        )
        Intents.intending(matcher)

        onView(
            RecyclerViewMatcher(R.id.recycleRepositories)
                .atPosition(0)
        ).perform(click())

        Intents.intended(matcher)

        Intents.release()
    }

    private fun mockRepository() = Repository(
        id = 121395510,
        full_name = "CyC2018/CS-Notes",
        name = "CS-Notes",
        description = ":books: 技术面试必备基础知识、Leetcode、计算机操作系统、计算机网络、系统设计、Java、Python、C++",
        owner = mockOwner(),
        stargazers_count = 104762,
        forks = 34169
    )

    private fun mockOwner() = Owner(
        login = "CyC2018",
        avatar_url = "https://avatars3.githubusercontent.com/u/36260787?v=4"
    )
}
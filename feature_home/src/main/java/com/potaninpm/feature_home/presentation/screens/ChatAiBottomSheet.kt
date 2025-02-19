package com.potaninpm.feature_home.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.potaninpm.feature_home.R
import com.potaninpm.feature_home.presentation.viewModels.ChatViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatAiBottomSheet(
    companyName: String,
    chatViewModel: ChatViewModel = koinViewModel(),
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    val scope = rememberCoroutineScope()
    val chatAnswer by chatViewModel.chatAns.collectAsState()
    val isThinking by chatViewModel.isThinking.collectAsState()
    val state = rememberScrollState()

    var countdown by remember { mutableIntStateOf(50) }

    LaunchedEffect(isThinking) {
        if (isThinking) {
            countdown = 50

            while (countdown > 0) {
                delay(1000)
                countdown--
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(state)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (chatAnswer == null) {
                Text(
                    text = "Что ИИ думает на счёт $companyName?",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (isThinking) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = stringResource(R.string.ai_is_thinking),
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = stringResource(R.string.approximate_time_waiting, countdown),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                } else {
                    Button(
                        onClick = {
                            scope.launch {
                                val systemLanguage = Locale.current.language
                                val request = if (systemLanguage == "ru") {
                                    "$companyName - краткое описание компании, включая ее основную деятельность и продукты. Рыночное положение: конкуренты - [перечислите основных конкурентов], доля рынка - [укажите долю рынка на соответствующем рынке], перспективы роста - [опишите ожидаемый рост рынка в ближайшие годы]. Риски и проблемы: скандалы - [упомяните о возможных скандалах или проблемах], регуляторные ограничения - [опишите регуляторные ограничения, с которыми может столкнуться компания], конкуренция - [обсудите уровень конкуренции на рынке], долговая нагрузка - [упомяните о долговой нагрузке компании]. Тренды и перспективы: [опишите текущие тренды и перспективы развития компании]. Оценка компании: P/E - [укажите P/E за последние годы], P/S - [укажите P/S за последние годы], EV/EBITDA - [укажите EV/EBITDA за последние годы], [добавьте комментарии аналитиков о переоцененности или недооцененности компании]. Мнение: [сформулируйте общее мнение о компании, учитывая ее потенциал роста, риски и финансовые показатели]."
                                } else {
                                    "$companyName - a brief description of the company, including its main activities and products. Market position: competitors - [list the main competitors], market share - [indicate the market share in the relevant market], growth prospects - [describe the expected market growth in the coming years]. Risks and challenges: scandals - [mention any scandals or issues], regulatory restrictions - [describe the regulatory restrictions the company may face], competition - [discuss the level of market competition], debt burden - [mention the company's debt burden]. Trends and prospects: [describe current trends and the company's development prospects]. Company evaluation: P/E - [specify P/E for recent years], P/S - [specify P/S for recent years], EV/EBITDA - [specify EV/EBITDA for recent years], [add analysts' comments on whether the company is overvalued or undervalued]. Opinion: [formulate an overall opinion about the company, considering its growth potential, risks, and financial performance]."
                                }

                                chatViewModel.getChatAnswer(request)
                                sheetState.expand()
                            }
                        }
                    ) {
                        Text("Анализ компании")
                    }
                }
            } else {
                Text(
                    text = "Ответ ИИ:",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = chatAnswer!!,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

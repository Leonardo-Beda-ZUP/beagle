//
//  Copyright © 08/10/19 Zup IT. All rights reserved.
//

import XCTest
import SnapshotTesting
@testable import BeagleUI

final class ButtonTests: XCTestCase {

    private let dependencies = BeagleScreenDependencies()
    
    func test_toView_shouldSetRightButtonTitle() {
        //Given
        let buttonTitle = "title"
        let component = Button(text: buttonTitle)
        let context = BeagleContextDummy()
        
        //When        
        guard let button = component.toView(context: context, dependencies: dependencies) as? UIButton else {
            XCTFail("Build View not returning UIButton")
            return
        }
        
        // Then
        XCTAssertEqual(button.titleLabel?.text, buttonTitle)
    }
    
    func test_toView_shouldApplyButtonStyle() {
        
        let theme = ThemeSpy()
        let dependencies = BeagleScreenDependencies(theme: theme)
        
        let style = "test.button.style"
        let button = Button(text: "apply style", style: style)
        
        let view = button.toView(context: BeagleContextDummy(), dependencies: dependencies)
        
        XCTAssertEqual(view, theme.styledView)
        XCTAssertEqual(style, theme.styleApplied)
    }
    
    func test_toView_shouldPrefetchNavigateAction() {
        let prefetch = BeaglePrefetchHelpingSpy()
        let dependencies = BeagleScreenDependencies(preFetchHelper: prefetch)
        
        let navigatePath = "path-to-prefetch"
        let navigate = Navigate.presentView(.init(path: navigatePath))
        let button = Button(text: "prefetch", action: navigate)
        
        _ = button.toView(context: BeagleContextDummy(), dependencies: dependencies)
        XCTAssertEqual([navigatePath], prefetch.prefetched)
    }
    
    func test_action_shouldBeTriggered() {
        
        let action = ActionDummy()
        let button = Button(text: "Trigger Action", action: action)
        let context = BeagleContextSpy()
        
        let view = button.toView(context: context, dependencies: dependencies)
        (view as? Button.BeagleUIButton)?.triggerAction()
        
        XCTAssertEqual(context.actionCalled as? ActionDummy, action)
    }
    
    func test_whenDecodingJson_thenItShouldReturnAButton() throws {
        let component: Button = try componentFromJsonFile(fileName: "buttonComponent")
        assertSnapshot(matching: component, as: .dump)
    }
}

final class ThemeSpy: Theme {
    
    private(set) var styledView: UIView?
    private(set) var styleApplied: String?
    
    func applyStyle<T>(for view: T, withId id: String) where T: UIView {
        styledView = view
        styleApplied = id
    }
}

final class BeaglePrefetchHelpingSpy: BeaglePrefetchHelping {
    
    private(set) var prefetched: [String] = []
    private(set) var dequeued: [String] = []
    var maximumScreensCapacity = 30
    
    func prefetchComponent(newPath: Navigate.NewPath, dependencies: Dependencies) {
        
        prefetched.append(newPath.path)
    }
    
    func dequeueComponent(path: String) -> ServerDrivenComponent? {
        dequeued.append(path)
        return nil
    }
}
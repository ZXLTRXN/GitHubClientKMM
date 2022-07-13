//
//  AuthViewController.swift
//  GitHubClient
//
//  Created by Ilya Shevtsov on 17.06.2022.
//

import UIKit
import MaterialComponents.MaterialActivityIndicator
import shared

class AuthViewController: UIViewController {
    
    @IBOutlet private weak var tokenTextField: UITextField!
    @IBOutlet private weak var errorLabel: UILabel!
    @IBOutlet private weak var signInButton: UIButton!
    @IBOutlet private weak var activityIndicator: MDCActivityIndicator!
    
    private let appRepo = DI.shared.appRepo
    
    private var editState: EditState = .valid
    private var validationEnabled = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        tokenTextField.delegate = self
        setUI()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        navigationController?.setNavigationBarHidden(true, animated: animated)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        navigationController?.setNavigationBarHidden(false, animated: animated)
    }
    
    private func setUI() {
        tokenTextField.placeholder = NSLocalizedString("auth.tokenTextField.placeholder", comment: "")
        tokenTextField.setBorderColor()
        
        signInButton.setTitle(NSLocalizedString("auth.signInButton.title", comment: ""), for: .normal)
        signInButton.setTitleColor(signInButton.backgroundColor, for: .disabled)
        activityIndicator.setColor()
    }
    
    private func onLoading(started flag: Bool) {
        tokenTextField.isEnabled = !flag
        signInButton.isEnabled = !flag
        if flag {
            activityIndicator.show()
        } else {
            activityIndicator.hide()
        }
    }
    
    @IBAction private func signInTapped(_ sender: UIButton) {
        if !validationEnabled {
            validationEnabled = true
            checkValidation(onValid: setIdleState)
        }
        if editState == .valid {
            guard let token = tokenTextField.text else { return }
            onLoading(started: true)
            
            appRepo.signIn(token: token){ [weak self] error in
                self?.onLoading(started: false)
                guard let error = error else {
                     self?.navigationController?.setViewControllers([RepositoriesListViewController()], animated: true)
                    return
                }
                self?.showAlert(for: error.asCustomError(), sender: sender)
            }
        }
    }
    
    @IBAction private func editingChanged(_ sender: UITextField) {
        checkValidation(onValid: setIdleState)
    }
    
    private func checkValidation(onValid: () -> Void) {
        if validationEnabled {
            editState = validateToken(tokenTextField.text)
            switch (editState) {
            case .invalid:
                setTokenErrorState(message: NSLocalizedString("auth.inputErrorLabel.invalidToken.title", comment: ""))
            case .empty:
                setTokenErrorState(message: NSLocalizedString("auth.inputErrorLabel.emptyToken.title", comment: ""))
            case .valid:
                onValid()
            }
        } else {
            onValid()
        }
    }
    
    private func validateToken(_ token: String?) -> EditState {
        guard token?.isEmpty == false else {
            return .empty
        }
        let regularExpression = "[A-Za-z0-9_]{20,40}"
        let predicate = NSPredicate(format:"SELF MATCHES %@", regularExpression)
        if predicate.evaluate(with: token){
            return .valid
        } else {
            return .invalid
        }
    }
    
    private func setTokenErrorState(message: String) {
        tokenTextField.setBorderColor(UIColor(named: "ErrorRed")?.cgColor)
        errorLabel.isHidden = false
        errorLabel.text = message
    }
    
    private func setIdleState() {
        tokenTextField.setBorderColor(UIColor(named: "DefaultBlue")?.cgColor)
        errorLabel.isHidden = true
        errorLabel.text = nil
    }
}

extension AuthViewController: UITextFieldDelegate {
    func textFieldDidBeginEditing(_ textField: UITextField) {
        checkValidation(onValid: setIdleState)
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        checkValidation(){
            textField.setBorderColor()
        }
    }
}

enum EditState: Equatable {
    case empty
    case invalid
    case valid
}
